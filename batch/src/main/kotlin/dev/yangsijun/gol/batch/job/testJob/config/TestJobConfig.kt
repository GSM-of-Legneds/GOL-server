package dev.yangsijun.gol.batch.job.testJob.config

import dev.yangsijun.gol.batch.job.testJob.parameter.TestJobParameter
import dev.yangsijun.gol.common.logger.LoggerDelegator
import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.StepContribution
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepScope
import org.springframework.batch.core.job.builder.JobBuilder
import org.springframework.batch.core.launch.JobLauncher
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.repository.JobRepository
import org.springframework.batch.core.scope.context.ChunkContext
import org.springframework.batch.core.step.builder.StepBuilder
import org.springframework.batch.item.Chunk
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.support.ListItemReader
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.PlatformTransactionManager


@Configuration
class TestJobConfig(
    val jobLauncher: JobLauncher,
    val jobRepository: JobRepository,
    val batchTransactionManager: PlatformTransactionManager,
    val parameter: TestJobParameter
) {
    val log by LoggerDelegator()

    // --JOB_NAME=test_job DATE_TIME=2022/06/18-21:31:56-KST

    companion object {
        val CHUNK_SIZE: Int = 100
        val JOB_NAME: String = "test_job"
    }

    @Bean
    @JobScope
    fun parameter(
        @Value("#{jobParameters[VERSION]}") version: Int
    ): TestJobParameter {
        return TestJobParameter(version)
    }

    @Bean
    fun testJob(): Job {
        return JobBuilder(JOB_NAME, jobRepository)
            .preventRestart() // 같은 파라미터 재실행 방지
            .start(chunkStep())
            .next(taskletStep())
            .build()
    }

    @Bean
    @JobScope
    fun taskletStep(): Step {
        return StepBuilder("first step", jobRepository)
            .tasklet({ stepContribution: StepContribution?, chunkContext: ChunkContext ->
                log.info("This is first tasklet step")
                log.info("SEC = {}", chunkContext.stepContext.stepExecutionContext)
                RepeatStatus.FINISHED
            }, batchTransactionManager).build()
    }

    @Bean
    @JobScope
    fun chunkStep(): Step {
        return StepBuilder("first step", jobRepository)
            .chunk<String, String>(CHUNK_SIZE, batchTransactionManager)
            .reader(reader())
            .writer(writer())
            .build()
    }

    @Bean
    @StepScope
    fun reader(): ItemReader<String> {
        val data: List<String> = mutableListOf(
            "Byte",
            "Code",
            "Data",
            "Disk",
            "File",
            "Input",
            "Loop",
            "Logic",
            "Mode",
            "Node",
            "Port",
            "Query",
            "Ratio",
            "Root",
            "Route",
            "Scope",
            "Syntax",
            "Token",
            "Trace"
        )
        return ListItemReader(data)
    }

    @Bean
    @StepScope
    fun writer(): ItemWriter<String> {
        return ItemWriter<String> { items: Chunk<out String> ->
            for (item in items) {
                log.info("Writing item: {}", item)
            }
        }
    }
}
