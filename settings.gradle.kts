pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

rootProject.name = "clean-url-shortener"
include("shortlink:shortlink-domain")
include("shortlink:shortlink-application")

findProject(":shortlink:shortlink-domain")?.name = "shortlink-domain"



findProject(":shortlink:shortlink-application")?.name = "shortlink-application"


include("shortlink:shortlink-adapters")
findProject(":shortlink:shortlink-adapters")?.name = "shortlink-adapters"

include("e2e")
include("bootstrap-spring")
include("shortlink:shortlink-application-impl")
include("identity:identity-adapters")
include("identity:identity-application")
include("identity:identity-application-impl")
include("identity:identity-domain")