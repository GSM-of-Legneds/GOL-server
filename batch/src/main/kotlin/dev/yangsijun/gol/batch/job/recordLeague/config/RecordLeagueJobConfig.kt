package dev.yangsijun.gol.batch.job.renewSummoner.config

import dev.yangsijun.gol.batch.common.exception.GolBatchException
import dev.yangsijun.gol.common.entity.league.League
import dev.yangsijun.gol.common.entity.summoner.Summoner
import dev.yangsijun.gol.common.entity.user.User
import dev.yangsijun.gol.common.logger.LoggerDelegator
import dev.yangsijun.gol.common.riot.api.RiotLeagueBySummonerIdApi
import dev.yangsijun.gol.common.riot.dto.LeagueApiResponse
import dev.yangsijun.gol.common.riot.exception.RiotException
import dev.yangsijun.gol.common.riot.mapper.ApiToLeagueMapper
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
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.transaction.PlatformTransactionManager
import java.util.*


@Configuration
class RecordLeagueJobConfig(
    val jobLauncher: JobLauncher,
    val jobRepository: JobRepository,
    val batchTransactionManager: PlatformTransactionManager,
    val mongoTemplate: MongoTemplate,
    val riotLeagueBySummonerIdApi: RiotLeagueBySummonerIdApi
) {
    val log by LoggerDelegator()


    // --JOB_NAME=record_league_job VERSION=1 DATE_TIME=2022/06/18-21:31:56-KST

    companion object {
        const val CHUNK_SIZE: Int = 40
        const val JOB_NAME: String = "record_league_job"
    }

    @Bean
    fun recordLeagueJob(): Job {
        return JobBuilder(JOB_NAME, jobRepository)
            .preventRestart() // 같은 파라미터 재실행 방지
            .start(recordLeagueStep())
            .build()
    }

    @Bean
    @JobScope
    fun recordLeagueStep(): Step {
        return StepBuilder("record_league_step", jobRepository)
            .chunk<User, List<League>>(CHUNK_SIZE, batchTransactionManager)
            .reader(recordLeagueIR())
            .processor(recordLeagueIP())
            .writer(recordLeagueIW())
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
    fun recordLeagueIR(): MongoItemReader<User> {
        return MongoItemReaderBuilder<User>()
            .targetType(User::class.java)
            .template(mongoTemplate)
            .pageSize(CHUNK_SIZE)
            .jsonQuery("{  }")
            .sorts(Collections.singletonMap("_id", Sort.Direction.ASC))
            .name("renewUserIR")
            .build()
    }

    @Bean
    @StepScope
    fun recordLeagueIP(): ItemProcessor<User, List<League>> {
        return ItemProcessor<User, List<League>> { item ->
            log.debug("#recordLeagueIP - user : {}", item.toString())
            val summoners: List<Summoner> =
                mongoTemplate.find(Query(Criteria.where("userId").`is`(item.id)), Summoner::class.java)
            if (summoners.isEmpty())
                throw GolBatchException("Summoner를 등록하지 않은 유저입니다. User ID" + item.id)
            val leagues: MutableList<League> = mutableListOf()
            for (summoner in summoners) {
                val summonerLeagues = recordLeague(summoner)
                leagues.addAll(summonerLeagues)
                log.trace("#recordLeagueIP - summonerLeagues : {}", summonerLeagues)
            }
            log.debug("#recordLeagueIP - leagues : {}", leagues)
            leagues
        }
    }

    @Bean
    @StepScope
    fun recordLeagueIW(): ItemWriter<List<League>> {
        return ItemWriter<List<League>> { chunk ->
            for (leagues in chunk) {
                mongoTemplate.insertAll(leagues)
            }
        }
    }

    fun recordLeague(summoner: Summoner): List<League> {
        val rss: List<LeagueApiResponse> = riotLeagueBySummonerIdApi.execute(summoner.summonerId, 500) // 0.5초 대기
        val leagues: MutableList<League> = mutableListOf()
        for (rs in rss) {
            leagues.add(ApiToLeagueMapper.responseToLeague(null, rs, summoner))
        }
        return leagues
    }
}
