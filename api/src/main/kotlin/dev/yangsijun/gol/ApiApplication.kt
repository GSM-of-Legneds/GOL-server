package dev.yangsijun.gol

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.config.EnableMongoAuditing

@SpringBootApplication
@EnableMongoAuditing
class ApiApplication

fun main(args: Array<String>) {
	runApplication<ApiApplication>(*args)
}
