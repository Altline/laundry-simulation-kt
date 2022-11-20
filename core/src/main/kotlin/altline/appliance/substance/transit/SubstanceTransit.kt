package altline.appliance.substance.transit

import altline.appliance.measure.Volume
import altline.appliance.substance.MutableSubstance
import altline.appliance.transit.BasicConduit
import altline.appliance.transit.Conduit
import altline.appliance.transit.FlowDrain
import altline.appliance.transit.FlowSource

typealias SubstanceSource = FlowSource<Volume, MutableSubstance>
typealias SubstanceDrain = FlowDrain<Volume, MutableSubstance>

typealias SubstanceSourcePort = FlowSource.Port<Volume, MutableSubstance>
typealias SubstanceDrainPort = FlowDrain.Port<Volume, MutableSubstance>

typealias SubstanceConduit = Conduit<Volume, MutableSubstance>
typealias BasicSubstanceConduit = BasicConduit<Volume, MutableSubstance>

