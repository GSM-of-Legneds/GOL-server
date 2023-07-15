package dev.yangsijun.gol.batch.job.recordMatch.parameter

import dev.yangsijun.gol.batch.common.parameter.BaseJobParameter
import java.time.LocalDateTime

open class RecordMatchJobParameter(
    version: Int,
    var dateTime: LocalDateTime,
    var startTime: LocalDateTime,
    var endTime: LocalDateTime
) : BaseJobParameter(version) {
}
