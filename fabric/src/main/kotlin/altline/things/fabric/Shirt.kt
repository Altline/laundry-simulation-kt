package altline.things.fabric

import altline.things.measure.Volume
import io.nacular.measured.units.*

class Shirt(
    size: Size,
    volume: Measure<Volume>
) : Clothing(size, volume)