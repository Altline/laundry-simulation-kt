import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm") version Libs.Kotlin.version
    id("org.jetbrains.compose") version Libs.Compose.version
}

group = "altline.appliance"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    api(platform(project(":platform")))

    api(project(":fabric"))
    api(project(":washing"))

    implementation(compose.desktop.currentOs)
    implementation(Libs.Koin.core)
    implementation(Libs.Logging.Log4j2.core)
    implementation(Libs.Logging.Log4j2.api)
    implementation(Libs.Logging.Log4j2.slf4jBridge)
}

compose.desktop {
    application {
        mainClass = "altline.appliance.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "appliance-simulation"
            packageVersion = "1.0.0"
        }
    }
}
