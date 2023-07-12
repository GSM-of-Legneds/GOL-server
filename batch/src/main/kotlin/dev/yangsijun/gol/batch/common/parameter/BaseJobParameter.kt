package dev.yangsijun.gol.batch.common.parameter

/*
    개발/QA 도중에 같은 파라미터 값으로 배치 Job 실행이 가능하도록 하는 역할
    version은 1부터 시작함
    https://jojoldu.tistory.com/487 - 3. 개발 & 운영 환경의 중복 파라미터 조건이 다를때 << 참고
*/
open class BaseJobParameter(val version: Int) {
}
