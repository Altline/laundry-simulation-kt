package altline.appliance.util

import mu.KLogger
import mu.KotlinLogging

fun <R : Any> R.logger(): Lazy<KLogger> {
    return lazy { KotlinLogging.logger(this::class.java.name) }
}