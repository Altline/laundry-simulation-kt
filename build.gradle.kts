plugins {
    kotlin("jvm") version Kotlin.version
    application
}

group = "altline.things"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    constraints {
        api(project(":util"))
        api(project(":measure"))
        api(project(":common"))
        api(project(":fabric"))
        api(project(":washing"))
    }

    api(platform(project(":fabric")))
    api(platform(project(":washing")))

    implementation(kotlin("stdlib"))
    implementation(KotlinxCoroutines.core)

    testImplementation(kotlin("test"))
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

application {
    mainClass.set("altline.things.MainKt")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
