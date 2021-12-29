package altline.things.electricity.transit

import altline.things.electricity.ElectricalEnergy
import altline.things.measure.Energy
import altline.things.transit.BasicConduit
import altline.things.transit.Conduit
import altline.things.transit.FlowDrain
import altline.things.transit.FlowSource

typealias ElectricalSource = FlowSource<Energy, ElectricalEnergy>
typealias ElectricalDrain = FlowDrain<Energy, ElectricalEnergy>
typealias ElectricalConduit = Conduit<Energy, ElectricalEnergy>
typealias BasicElectricalConduit = BasicConduit<Energy, ElectricalEnergy>