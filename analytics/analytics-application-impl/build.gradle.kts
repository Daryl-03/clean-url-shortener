plugins {
    id("java")
}

group = "dev.richryl"
version = "1.2.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    implementation(project(":analytics:analytics-application"))

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.mockito:mockito-core:5.5.0")
}

tasks.test {
    useJUnitPlatform()
}