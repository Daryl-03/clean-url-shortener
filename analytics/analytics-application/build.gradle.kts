plugins {
    id("java-library")
}

group = "dev.richryl"
version = "1.2.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api(project(":analytics:analytics-domain"))
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}