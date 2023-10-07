plugins {
    kotlin("jvm")
}

group = "altline.appliance"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    api(platform(project(":platform")))

    api(project(":util"))

    api(Libs.Measured.measured)
}