package altline.things.fabric

import altline.things.measure.Volume
import io.nacular.measured.units.Measure

class Shirt(
    size: Size,
    volume: Measure<Volume>
) : Clothing(size, volume)