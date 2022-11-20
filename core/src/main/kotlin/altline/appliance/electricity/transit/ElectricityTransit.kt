package altline.appliance.electricity.transit

import altline.appliance.electricity.MutableElectricalEnergy
import altline.appliance.measure.Energy
import altline.appliance.transit.BasicConduit
import altline.appliance.transit.Conduit
import altline.appliance.transit.FlowDrain
import altline.appliance.transit.FlowSource

typealias ElectricalSource = FlowSource<Energy, MutableElectricalEnergy>
typealias ElectricalDrain = FlowDrain<Energy, MutableElectricalEnergy>

typealias ElectricalSourcePort = FlowSource.Port<Energy, MutableElectricalEnergy>
typealias ElectricalDrainPort = FlowDrain.Port<Energy, MutableElectricalEnergy>

typealias ElectricalConduit = Conduit<Energy, MutableElectricalEnergy>
typealias BasicElectricalConduit = BasicConduit<Energy, MutableElectricalEnergy>