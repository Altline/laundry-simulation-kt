package altline.things.electricity.transit

import altline.things.electricity.MutableElectricalEnergy
import altline.things.measure.Energy
import altline.things.transit.BasicConduit
import altline.things.transit.Conduit
import altline.things.transit.FlowDrain
import altline.things.transit.FlowSource

typealias ElectricalSource = FlowSource<Energy, MutableElectricalEnergy>
typealias ElectricalDrain = FlowDrain<Energy, MutableElectricalEnergy>

typealias ElectricalSourcePort = FlowSource.Port<Energy, MutableElectricalEnergy>
typealias ElectricalDrainPort = FlowDrain.Port<Energy, MutableElectricalEnergy>

typealias ElectricalConduit = Conduit<Energy, MutableElectricalEnergy>
typealias BasicElectricalConduit = BasicConduit<Energy, MutableElectricalEnergy>