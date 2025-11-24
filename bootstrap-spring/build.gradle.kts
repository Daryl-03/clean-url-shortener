plugins {
    id("java")
    id("org.springframework.boot") version "3.5.6"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "dev.richryl"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    implementation(project(":shortlink:shortlink-application"))
    implementation(project(":shortlink:shortlink-application-impl"))
    implementation(project(":shortlink:shortlink-adapters"))

    implementation(project(":identity:identity-application"))
    implementation(project(":identity:identity-application-impl"))
    implementation(project(":identity:identity-adapters"))

    implementation(project(":analytics:analytics-application"))
    implementation(project(":analytics:analytics-application-impl"))
    implementation(project(":analytics:analytics-adapters"))

    implementation(project(":common-adapters"))

    implementation("org.springframework.security:spring-security-oauth2-jose")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")

//    spring boot
    implementation("org.springframework.boot:spring-boot-starter-web")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}