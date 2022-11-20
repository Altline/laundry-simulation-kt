@file:Suppress("MemberVisibilityCanBePrivate")

object Libs {
    object Kotlin {
        const val version = "1.7.20"
    }

    object Compose {
        const val version = "1.2.1"
    }

    object KotlinxCoroutines {
        const val version = "1.6.4"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
    }

    object Measured {
        const val version = "0.3.1"
        const val measured = "io.nacular.measured:measured:$version"
    }
}