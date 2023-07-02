plugins {}

dependencies {
	/* common */
	implementation(project(":common"))

	/* batch */
	implementation("org.springframework.boot:spring-boot-starter-batch")

	/* production */
	implementation("org.springframework.boot:spring-boot-starter-actuator")

	/* persistence */
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

	/* dev */
	developmentOnly("org.springframework.boot:spring-boot-docker-compose")

	/* test */
	testImplementation("org.springframework.security:spring-security-test")
}
