package dev.yangsijun.gol.batch.job.recordMatchIds.config

import dev.yangsijun.gol.batch.common.exception.GolBatchException
import dev.yangsijun.gol.batch.common.util.ParamUtil
import dev.yangsijun.gol.batch.job.recordMatch.cache.RecordMatchCache
import dev.yangsijun.gol.common.entity.match.Match
import dev.yangsijun.gol.common.entity.summoner.Summoner
import dev.yangsijun.gol.common.entity.user.User
import dev.yangsijun.gol.common.logger.LoggerDelegator
import dev.yangsijun.gol.common.riot.api.RiotMatchByMatchIdApi
import dev.yangsijun.gol.common.riot.api.RiotMatchIdsByPuuidApi
import dev.yangsijun.gol.common.riot.dto.MatchIdsApiRequest
import dev.yangsijun.gol.common.riot.exception.RiotException
import dev.yangsijun.gol.common.riot.mapper.ApiToMatchMapper
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
import org.springframework.batch.item.data.MongoItemWriter
import org.springframework.batch.item.data.builder.MongoItemReaderBuilder
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder
import org.springframework.batch.item.support.ListItemReader
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.transaction.PlatformTransactionManager
import java.time.LocalDateTime
import java.util.*


@Configuration
class RecordMatchJobConfig(
    val jobLauncher: JobLauncher,
    val jobRepository: JobRepository,
    val batchTransactionManager: PlatformTransactionManager,
    val mongoTemplate: MongoTemplate,
    val cache: RecordMatchCache,
    val riotMatchIdsByPuuidApi: RiotMatchIdsByPuuidApi,
    val riotMatchByMatchIdApi: RiotMatchByMatchIdApi,
) {
    val log by LoggerDelegator()


    // --JOB_NAME=record_match_job VERSION=1 DATE_TIME=2022/06/18-21:31:56-KST START_TIME=2023/07/12-02:00:00-KST END_TIME=2023/07/13-02:00:00-KST

    companion object {
        const val CHUNK_SIZE: Int = 10
        const val JOB_NAME: String = "record_match_job"
        const val PARAMETER: String = JOB_NAME + "_parameter"
    }

    @Bean
    @JobScope
    fun recordMatchIdsCache(): RecordMatchCache {
        log.error("RecordMatchCache")
        return recordMatchIdsCache()
    }

    @Bean
    fun recordMatchJob(): Job {
        return JobBuilder(JOB_NAME, jobRepository)
            .preventRestart() // 같은 파라미터 재실행 방지
            .start(recordMatchIdsStep())
            .next(recordMatchStep())
            .build()
    }

    @Bean
    @JobScope
    fun recordMatchIdsStep(): Step {
        return StepBuilder("record_matchIds_step", jobRepository)
            .chunk<User, Set<String>>(CHUNK_SIZE, batchTransactionManager)
            .reader(recordMatchIdsIR())
            .processor(recordMatchIdsIP(null, null))
            .writer(recordMatchIdsIW())
            .faultTolerant()
            .noSkip(RiotException::class.java) // 라이엇 api 발생 시 skip 없이 바로 실패
            .skip(GolBatchException::class.java).skipLimit(10)
            .listener(object : SkipListener<User, User> {
                override fun onSkipInProcess(user: User, t: Throwable) {
                    log.warn("{} Occur - {}, User : {}", t.javaClass.simpleName, t.message, user)
                }
            })
            .build()
    }

    @Bean
    @JobScope
    fun recordMatchStep(): Step {
        return StepBuilder("record_match_step", jobRepository)
            .chunk<String, Match>(CHUNK_SIZE, batchTransactionManager)
            .reader(recordMatchIR())
            .processor(recordMatchIP())
            .writer(recordMatchIW())
            .faultTolerant()
            .noSkip(RiotException::class.java) // 라이엇 api 발생 시 skip 없이 바로 실패
            .skip(GolBatchException::class.java).skipLimit(10)
            .listener(object : SkipListener<String, Match> {
                override fun onSkipInProcess(matchId: String, t: Throwable) {
                    log.warn("{} Occur - {}, MatchId : {}", t.javaClass.simpleName, t.message, matchId)
                }
            })
            .build()
    }


    @Bean
    @StepScope
    fun recordMatchIdsIR(): MongoItemReader<User> {
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
    fun recordMatchIdsIP(
        @Value("#{jobParameters[START_TIME]}") strStartTime: String?,
        @Value("#{jobParameters[END_TIME]}") strEndTime: String?
    ): ItemProcessor<User, Set<String>> {
        val startTime: LocalDateTime = ParamUtil.strDateTimeToLocalDateTime(strStartTime!!)
        val endTime: LocalDateTime = ParamUtil.strDateTimeToLocalDateTime(strEndTime!!)
        return ItemProcessor<User, Set<String>> { item ->
            log.debug("#recordMatchIdsIP - user : {}", item.toString())
            val summoner: Summoner =
                mongoTemplate.findOne(Query(Criteria.where("userId").`is`(item.id)), Summoner::class.java)
                    ?: throw GolBatchException("Summoner를 등록하지 않은 유저입니다. User ID" + item.id)
            log.debug("#recordMatchIdsIP - old summoner : {}", summoner.toString())
            getMatchIds(summoner, startTime, endTime)
        }
    }

    @Bean
    @StepScope
    fun recordMatchIdsIW(): ItemWriter<Set<String>> {
        return ItemWriter<Set<String>> { chunk ->
            for (item in chunk) {
                for (i in item) {
                    cache.add(i)
                }
            }
        }
    }

    fun getMatchIds(
        summoner: Summoner,
        startTime: LocalDateTime,
        endTime: LocalDateTime
    ): Set<String> {
        val matchIds: MutableList<String> = mutableListOf()
        var start = 0
        val count = 100 // 가져올 개수 (100 이하일 수 있음)
        var preCount = 0 // 이전 요청에서 가져온 개수
        do {
            val request = MatchIdsApiRequest(
                puuid = summoner.puuid,
                start = start,
                count = count,
                startTime = startTime,
                endTime = endTime
            )
            val rs: List<String> = riotMatchIdsByPuuidApi.execute(request, 500) // 0.5초 대기
            matchIds.addAll(rs)
            // 가져온 id 수가 count와 나누어고,가져온 개수가 이전과 같지 않으면 계속 가져오기
            val isEnd = ((matchIds.count() / count) == 0) and (matchIds.count() != preCount)
            start = count
            preCount = matchIds.count()
        } while (!isEnd) // 끝나는 조건을 만족하지 않을 때까지 반복
        return matchIds.toSet()
    }

    @Bean
    @StepScope
    fun recordMatchIR(): ListItemReader<String> {
        return ListItemReader<String>(cache.toList())
    }

    @Bean
    @StepScope
    fun recordMatchIP(): ItemProcessor<String, Match?> {
        return object : ItemProcessor<String, Match?> {
            override fun process(item: String): Match? {
                log.debug("recordMatchIP - matchId : {}", item)
                val savedMatch:Match? = mongoTemplate.findOne(Query(Criteria.where("data.metadata.matchId").`is`(item)), Match::class.java)
                if (savedMatch != null) {
                    log.warn("Saved Match!! MatchId : {}",item)
                    return null
                }
                // value가 map이 아니면 내부 데이터를 찾을 수가 없음 -> 형변환해서 받기
                val rs: Map<String, Map<Any, Any>> =
                    riotMatchByMatchIdApi.execute(item, 500) as Map<String, Map<Any, Any>>
                val gamePlayersPuuid: List<String> = rs.get("metadata")?.get("participants") as List<String>
                log.debug("#recordMatchIP - game players puuid : {}", gamePlayersPuuid)
                // 게임 플레이어 중 서비스에 등록한 Summoner 가져오기
                val players: List<Summoner> = mongoTemplate.find(Query(Criteria.where("puuid").`in`(gamePlayersPuuid)), Summoner::class.java)
                log.trace("#recordMatchIP - summoner : {}", players)
                val match: Match = ApiToMatchMapper.responseToMatch(null, rs, players)
                return match
            }
        }
    }

    @Bean
    @StepScope
    fun recordMatchIW(): MongoItemWriter<Match> {
        return MongoItemWriterBuilder<Match>()
            .template(mongoTemplate)
            .build()
    }

}
