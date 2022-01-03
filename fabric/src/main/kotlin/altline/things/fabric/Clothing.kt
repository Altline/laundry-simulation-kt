package altline.things.fabric

import altline.things.measure.Volume
import io.nacular.measured.units.*

abstract class Clothing(
    val size: Size,
    volume: Measure<Volume>
) : Fabric(volume) {

    enum class Size {
        XXS, XS, S, M, L, XL, XXL
    }
}