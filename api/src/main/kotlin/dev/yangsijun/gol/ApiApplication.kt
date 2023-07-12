package dev.yangsijun.gol

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy
import org.springframework.data.mongodb.config.EnableMongoAuditing

@SpringBootApplication
@EnableMongoAuditing
@EnableAspectJAutoProxy
class ApiApplication

fun main(args: Array<String>) {
	runApplication<ApiApplication>(*args)
}
