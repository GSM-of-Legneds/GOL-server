plugins {}

dependencies {
	/* common */
	implementation(project(":common"))

	/* web */
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	/* validation */
	implementation("org.springframework.boot:spring-boot-starter-validation")

	/* security */
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("com.github.YangSiJun528:GAuth-spring-boot-starter:1.0.1")

	/* production */
	implementation("org.springframework.boot:spring-boot-starter-actuator")

	/* persistence */
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

	/* */
	implementation("org.springframework.boot:spring-boot-starter-aop")

	/* test */
	testImplementation("org.springframework.security:spring-security-test")
}
