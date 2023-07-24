package altline.appliance.fabric.clothing

import altline.appliance.measure.Volume
import io.nacular.measured.units.*

class Shirt(
    size: Size,
    volume: Measure<Volume>
) : Clothing(size, volume)