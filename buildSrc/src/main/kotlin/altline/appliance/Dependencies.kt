@file:Suppress("MemberVisibilityCanBePrivate")

object Libs {
    object Kotlin {
        const val version = "1.7.20"
    }

    object Compose {
        const val version = "1.3.0-beta03"
    }

    object KotlinxCoroutines {
        const val version = "1.6.4"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
    }

    object Koin {
        const val version = "3.2.2"
        const val core = "io.insert-koin:koin-core:$version"
    }

    object Measured {
        const val version = "0.3.1"
        const val measured = "io.nacular.measured:measured:$version"
    }
}