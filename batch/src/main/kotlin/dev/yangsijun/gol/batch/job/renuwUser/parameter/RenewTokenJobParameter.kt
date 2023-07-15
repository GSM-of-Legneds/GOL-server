package dev.yangsijun.gol.batch.job.renuwUser.parameter

import dev.yangsijun.gol.batch.common.parameter.BaseJobParameter
import java.time.LocalDateTime

open class RenewTokenJobParameter(version: Int, val dateTime: LocalDateTime) : BaseJobParameter(version) {
}
