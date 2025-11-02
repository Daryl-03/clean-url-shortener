plugins {
    id("java")
}

group = "dev.richryl"
version = "1.1.0"

allprojects {
    repositories { mavenCentral() }
}

dependencies {
    implementation(project(":shortlink:shortlink-domain"))
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.register("printVersion") {
    // Cette tâche sera utilisée par GitHub Actions pour extraire la version.
    doLast {
        println(project.version)
    }
}

tasks.test {
    useJUnitPlatform()
}