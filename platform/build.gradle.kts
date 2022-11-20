plugins {
    `java-platform`
}

dependencies {
    constraints {
        api(project(":util"))
        api(project(":core"))
        api(project(":fabric"))
    }
}