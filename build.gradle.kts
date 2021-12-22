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

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}
