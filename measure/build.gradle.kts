plugins {
    kotlin("jvm")
    java
}

group = "altline.things"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api(platform(project(":util")))

    implementation(kotlin("stdlib"))
    implementation(Measured.measured)

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}