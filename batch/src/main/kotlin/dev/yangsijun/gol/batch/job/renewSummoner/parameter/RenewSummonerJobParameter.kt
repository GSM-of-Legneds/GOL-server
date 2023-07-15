package dev.yangsijun.gol.batch.job.renewSummoner.parameter

import dev.yangsijun.gol.batch.common.parameter.BaseJobParameter
import java.time.LocalDateTime

open class RenewSummonerJobParameter(version: Int, val dateTime: LocalDateTime) : BaseJobParameter(version) {
}
