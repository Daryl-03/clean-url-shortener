plugins {
    id("java")
    id("org.springframework.boot") version "3.5.6"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "dev.richryl"
version = "1.2.0-SNAPSHOT"

repositories {
    mavenCentral()
}
val mockitoAgent: Configuration by configurations.creating

configurations {
    mockitoAgent
}

dependencies {
    implementation(project(":analytics:analytics-application"))
    implementation("org.springframework.boot:spring-boot-starter-security")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    implementation("org.springframework.security:spring-security-oauth2-jose:7.0.0-RC3")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
    implementation("nl.basjes.parse.useragent:yauaa:7.32.0")
    implementation("com.maxmind.geoip2:geoip2:5.0.0")

    testImplementation("org.springframework.security:spring-security-test")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.springframework.security:spring-security-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.mockito:mockito-core")
    mockitoAgent("org.mockito:mockito-core") {
        isTransitive = false
    }
}

tasks.test {
    useJUnitPlatform()
    jvmArgs("-javaagent:${configurations["mockitoAgent"].asPath}")

}