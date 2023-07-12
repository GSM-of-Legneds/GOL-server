package dev.yangsijun.gol.batch.job.testJob.parameter

import dev.yangsijun.gol.batch.common.parameter.BaseJobParameter

// CGLIB 서브클래스를 생성할 수 있게 open
open class TestJobParameter(version: Int) : BaseJobParameter(version) {
}
