package dev.yangsijun.gol.batch.job.renewSummoner.config

import dev.yangsijun.gol.batch.common.exception.GolBatchException
import dev.yangsijun.gol.batch.common.util.ParamUtil
import dev.yangsijun.gol.batch.job.renewSummoner.parameter.RenewSummonerJobParameter
import dev.yangsijun.gol.common.entity.summoner.Summoner
import dev.yangsijun.gol.common.entity.user.User
import dev.yangsijun.gol.common.logger.LoggerDelegator
import dev.yangsijun.gol.common.riot.api.RiotSummonerByPuuidApi
import dev.yangsijun.gol.common.riot.dto.SummonerApiResponse
import dev.yangsijun.gol.common.riot.exception.RiotException
import dev.yangsijun.gol.common.riot.mapper.ApiToSummonerMapper
import org.springframework.batch.core.Job
import org.springframework.batch.core.SkipListener
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.data.MongoItemReader
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.transaction.PlatformTransactionManager
import java.util.*


/*
저장된 GAuth Token을 갱신하고,
갱신된 GAuth Token으로 사용자 정보를 갱신하는 배치
TODO 졸업하거나 삭제한 GAuth User, Token이 남아있지 않도록 수정해야 함
*/
@Configuration
class RenewSummonerJobConfig(
    val jobLauncher: JobLauncher,
    val jobRepository: JobRepository,
    val batchTransactionManager: PlatformTransactionManager,
    val mongoTemplate: MongoTemplate,
    val paramaeter: RenewSummonerJobParameter,
    val riotSummonerByPuuidApi: RiotSummonerByPuuidApi
) {
    val log by LoggerDelegator()


    // --JOB_NAME=renew_summoner_job VERSION=1 DATE_TIME=2022/06/18-21:31:56-KST

    companion object {
        const val CHUNK_SIZE: Int = 40
        const val JOB_NAME: String = "renew_summoner_job"
        const val PARAMETER: String = JOB_NAME + "_parameter"
    }

    @Bean(PARAMETER)
    @JobScope
    fun parameter(
        @Value("#{jobParameters[VERSION]}") version: Int,
        @Value("#{jobParameters[DATE_TIME]}") strDateTime: String
    ): RenewSummonerJobParameter {
        return RenewSummonerJobParameter(
            version = version,
            dateTime = ParamUtil.strDateTimeToLocalDateTime(strDateTime)
        )
    }

    @Bean
    fun renewSummonerJob(): Job {
        return JobBuilder(JOB_NAME, jobRepository)
            .preventRestart() // 같은 파라미터 재실행 방지
            .start(renewSummonerStep())
            .build()
    }

    @Bean
    @JobScope
    fun renewSummonerStep(): Step {
        return StepBuilder("renew_summoner_step", jobRepository)
            .chunk<User, List<Summoner>>(CHUNK_SIZE, batchTransactionManager)
            .reader(renewSummonerIR())
            .processor(renewSummonerIP())
            .writer(renewSummonerIW())
            .faultTolerant()
            .noSkip(RiotException::class.java) // 라이엇 api 발생 시 skip 없이 바로 실패
            .skip(GolBatchException::class.java).skipLimit(10)
            .listener(object : SkipListener<User, Summoner> {
                override fun onSkipInProcess(user: User, t: Throwable) {
                    log.warn("{} Occur - {}, User : {}", t.javaClass.simpleName, t.message, user)
                }
            })
            .build()
    }

    @Bean
    @StepScope
    fun renewSummonerIR(): MongoItemReader<User> {
        return MongoItemReaderBuilder<User>()
            .targetType(User::class.java)
            .template(mongoTemplate)
            .pageSize(CHUNK_SIZE)
            .jsonQuery("{ removed : false }")
            .sorts(Collections.singletonMap("_id", Sort.Direction.ASC))
            .name("renewUserIR")
            .build()
    }

    @Bean
    @StepScope
    fun renewSummonerIP(): ItemProcessor<User, List<Summoner>> {
        return ItemProcessor<User, List<Summoner>> { item ->
            log.debug("#renewSummonerIP - user : {}", item.toString())
            val oldSummoners: List<Summoner> =
                mongoTemplate.find(Query(Criteria.where("userId").`is`(item.id)), Summoner::class.java)
            if (oldSummoners.isEmpty())
                throw GolBatchException("Summoner를 등록하지 않은 유저입니다. User ID" + item.id)
            log.debug("#renewSummonerIP - old summoners : {}", oldSummoners)
            val renewedSummoners = mutableListOf<Summoner>()
            for (oldSummoner in oldSummoners) {
                val renewedSummoner: Summoner = renewSummoner(oldSummoner)
                log.debug("#renewSummonerIP - new summoner : {}", renewedSummoner.toString())
                renewedSummoners.add(renewedSummoner)
            }
            renewedSummoners
        }
    }

    @Bean
    @StepScope
    fun renewSummonerIW(): ItemWriter<List<Summoner>> {
        return ItemWriter<List<Summoner>> { chunk ->
            for (summoners in chunk) {
                for (summoner in summoners) {
                    val update = Update()
                    update
                        .set("name", summoner.name)
                        .set("profileIconId", summoner.profileIconId)
                        .set("revisionDate", summoner.revisionDate)
                        .set("summonerLevel", summoner.summonerLevel)
                    mongoTemplate.updateMulti(
                        Query.query(Criteria.where("_id").`is`(summoner.id)),
                        update,
                        Summoner::class.java
                    )
                }
            }
        }
    }

    fun renewSummoner(summoner: Summoner): Summoner {
        val rs: SummonerApiResponse = riotSummonerByPuuidApi.execute(summoner.puuid, 500) // 0.5초 대기
        return ApiToSummonerMapper.responseToSummoner(summoner.id, rs, summoner.user)
    }
}
