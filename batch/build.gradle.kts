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

	/* test */
	testImplementation("org.springframework.security:spring-security-test")
}
