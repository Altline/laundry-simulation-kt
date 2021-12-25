package altline.things.common

import altline.things.measure.Volume
import io.nacular.measured.units.Measure

interface Body {
    val volume: Measure<Volume>
}