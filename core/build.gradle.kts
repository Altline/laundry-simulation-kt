plugins {
    kotlin("jvm")
}

group = "altline.appliance"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api(platform(project(":platform")))

    api(project(":util"))

    api(Libs.Measured.measured)
}