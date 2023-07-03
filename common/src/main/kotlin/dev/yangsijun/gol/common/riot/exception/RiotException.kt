package dev.yangsijun.gol.common.riot.exception

import org.springframework.http.HttpStatus

class RiotException(message: String, httpStatus: HttpStatus, cause: Throwable) : RuntimeException(message, cause) {
    constructor(message: String, httpStatus: HttpStatus) : this(message, httpStatus, Throwable())
}
