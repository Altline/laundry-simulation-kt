package altline.appliance.fabric

import altline.appliance.measure.Volume
import io.nacular.measured.units.*

abstract class Clothing(
    val size: Size,
    volume: Measure<Volume>
) : Fabric(volume) {

    enum class Size {
        XXS, XS, S, M, L, XL, XXL
    }
}