package dev.yangsijun.gol.batch.job.renuwUser.config

import dev.yangsijun.gol.batch.common.exception.GolBatchException
import dev.yangsijun.gol.batch.common.util.ParamUtil
import dev.yangsijun.gol.batch.job.renuwUser.cache.RenewTokenCache
import dev.yangsijun.gol.batch.job.renuwUser.parameter.RenewTokenJobParameter
import dev.yangsijun.gol.common.entity.token.Token
import dev.yangsijun.gol.common.entity.user.User
import dev.yangsijun.gol.common.logger.LoggerDelegator
import gauth.GAuth
import gauth.GAuthToken
import gauth.GAuthUserInfo
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
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.Sort
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.transaction.PlatformTransactionManager
import java.util.*


/*
저장된 GAuth Token을 갱신하고,
갱신된 GAuth Token으로 사용자 정보를 갱신하는 배치
TODO 졸업하거나 삭제한 GAuth User, Token이 남아있지 않도록 수정해야 함
*/
@Configuration
class RenewTokenJobConfig(
    val jobLauncher: JobLauncher,
    val jobRepository: JobRepository,
    val batchTransactionManager: PlatformTransactionManager,
    val mongoTemplate: MongoTemplate,
    val paramaeter: RenewTokenJobParameter,
    val gauth: GAuth,
    val cache: RenewTokenCache
) {
    val log by LoggerDelegator()


    // --JOB_NAME=renew_user_job VERSION=1 DATE_TIME=2022/06/18-21:31:56-KST

    companion object {
        const val CHUNK_SIZE: Int = 40
        const val JOB_NAME: String = "renew_user_job"
        const val PARAMETER: String = JOB_NAME + "_parameter"
    }

    @Bean(PARAMETER)
    @JobScope
    fun parameter(
        @Value("#{jobParameters[VERSION]}") version: Int,
        @Value("#{jobParameters[DATE_TIME]}") strDateTime: String
    ): RenewTokenJobParameter {
        return RenewTokenJobParameter(
            version = version,
            dateTime = ParamUtil.strDateTimeToLocalDateTime(strDateTime)
        )
    }

    @Bean
    @JobScope
    fun cache(): RenewTokenCache {
        return RenewTokenCache()
    }

    @Bean
    fun renewUserJob(): Job {
        return JobBuilder(JOB_NAME, jobRepository)
            .preventRestart()
            .start(renewTokenStep())
            .next(renewUserStep())
            .build()
    }

    @Bean
    @JobScope
    fun renewTokenStep(): Step {
        return StepBuilder("renew_token_step", jobRepository)
            .chunk<Token, Token>(CHUNK_SIZE, batchTransactionManager)
            .reader(renewTokenIR())
            .writer(renewTokenIW())
            .build()
    }

    @Bean
    @StepScope
    fun renewTokenIR(): MongoItemReader<Token> {

        return MongoItemReaderBuilder<Token>()
            .targetType(Token::class.java)
            .template(mongoTemplate)
            .pageSize(CHUNK_SIZE)
            .jsonQuery("{  }")
            .sorts(Collections.singletonMap("_id", Sort.Direction.ASC))
            .name("renewTokenIR")
            .build()
    }

    @Bean
    @StepScope
    fun renewTokenIW(): ItemWriter<Token> {
        return ItemWriter<Token> { chunk ->
            for (item in chunk) {
                log.debug("#renewTokenIP - old token : {}", item)
                val gauthToken: GAuthToken = renewToken(item.refreshToken)
                val refreshedToken = Token(
                    id = item.id,
                    gauthEmail = item.gauthEmail,
                    accessToken = gauthToken.accessToken,
                    refreshToken = gauthToken.refreshToken
                )
                log.debug("#renewTokenIP - refreshed token : {}", refreshedToken)
                cache.put(refreshedToken.gauthEmail, refreshedToken)
            }
        }
    }

    @Bean
    @JobScope
    fun renewUserStep(): Step {
        return StepBuilder("renew_user_step", jobRepository)
            .chunk<User, User>(CHUNK_SIZE, batchTransactionManager)
            .reader(renewUserIR())
            .processor(renewUserIP())
            .writer(renewUserIW())
            .faultTolerant()
            .skip(GolBatchException::class.java).skipLimit(10)
            .processorNonTransactional() // https://rmcodestar.github.io/spring%20batch/2020/05/21/spring-batch-skip-processorTransactional/ 참고
            .listener(object : SkipListener<User, User> {
                override fun onSkipInProcess(user: User, t: Throwable) {
                    log.warn("{} Occur - {}, User : {}", t.javaClass.simpleName, t.message, user)
                }
            })
            .build()
    }

    @Bean
    @StepScope
    fun renewUserIR(): MongoItemReader<User> {
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
    fun renewUserIP(): ItemProcessor<User, User?> {
        return ItemProcessor<User, User?> { item ->
            log.debug("#renewUserIP - old item : {}", item.toString())
            // 나중에 다른 커스텀 예외로 핸들링 + 졸업한 학생 처리하는 배치도 추가
            val token: Token = cache.get(item.email)
            // 테스트 때는 토큰이 없어서 예외남, 나도 핸들러기 처리해야 하는데...
                ?: throw GolBatchException("GAuth에 존재하지 않는 유저입니다. User Email : "+item.email)
            val gAuthUserInfo: GAuthUserInfo = renewUserInfo(token.accessToken)
            val user = User(
                id = item.id,
                email = gAuthUserInfo.email,
                name = gAuthUserInfo.name,
                grade = gAuthUserInfo.grade,
                classNum = gAuthUserInfo.classNum,
                num = gAuthUserInfo.num,
                gender = gAuthUserInfo.gender,
                profileUrl = gAuthUserInfo.profileUrl,
                role = gAuthUserInfo.role,
                removed = item.removed
            )
            log.debug("#renewUserIP - renew item : {}", user.toString())
            user

        }
    }

    @Bean
    @StepScope
    fun renewUserIW(): MongoItemWriter<User> {
        return MongoItemWriterBuilder<User>()
            .template(mongoTemplate)
            .build()
    }

    fun renewToken(refreshToken: String): GAuthToken {
        return gauth.refresh(refreshToken)
    }

    fun renewUserInfo(accessToken: String): GAuthUserInfo {
        return gauth.getUserInfo(accessToken)
    }
}
