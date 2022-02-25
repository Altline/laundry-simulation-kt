package altline.things.substance.transit

import altline.things.measure.Volume
import altline.things.substance.MutableSubstance
import altline.things.substance.Substance
import altline.things.transit.BasicConduit
import altline.things.transit.Conduit
import altline.things.transit.FlowDrain
import altline.things.transit.FlowSource

typealias SubstanceSource = FlowSource<Volume, MutableSubstance>
typealias SubstanceDrainPort = FlowDrain.Port<Volume, MutableSubstance>

typealias SubstanceDrain = FlowDrain<Volume, MutableSubstance>
typealias SubstanceSourcePort = FlowSource.Port<Volume, MutableSubstance>

typealias SubstanceConduit = Conduit<Volume, MutableSubstance>
typealias BasicSubstanceConduit = BasicConduit<Volume, MutableSubstance>

