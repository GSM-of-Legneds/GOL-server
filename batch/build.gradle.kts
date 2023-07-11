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
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.mariadb.jdbc:mariadb-java-client") // batch metadata 저장

	/* test */
	testImplementation("org.springframework.security:spring-security-test")
}
