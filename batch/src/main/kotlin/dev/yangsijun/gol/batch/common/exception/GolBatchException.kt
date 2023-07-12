package dev.yangsijun.gol.batch.common.exception


class GolBatchException : RuntimeException {
    constructor(message: String, cause: Throwable) : super(message, cause)
    constructor(message: String) : super(message)
}
