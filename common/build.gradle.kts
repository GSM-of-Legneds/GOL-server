import org.springframework.boot.gradle.tasks.bundling.BootJar

val jar: Jar by tasks
val bootJar: BootJar by tasks

jar.enabled = false
bootJar.enabled = false

plugins {}

dependencies {
    /* api */
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    /* persistence */
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("com.github.GSM-MSG:GAuth-SDK-Java:v2.0.1") // GAuthUserInfo 사용

    /* test */
    testImplementation("io.projectreactor:reactor-test")
}
