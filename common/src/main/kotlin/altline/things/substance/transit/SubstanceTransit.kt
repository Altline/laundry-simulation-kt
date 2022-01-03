package altline.things.substance.transit

import altline.things.measure.Volume
import altline.things.substance.Substance
import altline.things.transit.BasicConduit
import altline.things.transit.Conduit
import altline.things.transit.FlowDrain
import altline.things.transit.FlowSource

typealias SubstanceSource = FlowSource<Volume, Substance>
typealias SubstanceDrainPort = FlowDrain.Port<Volume, Substance>

typealias SubstanceDrain = FlowDrain<Volume, Substance>
typealias SubstanceSourcePort = FlowSource.Port<Volume, Substance>

typealias SubstanceConduit = Conduit<Volume, Substance>
typealias BasicSubstanceConduit = BasicConduit<Volume, Substance>

