package altline.things.electricity

import altline.things.electricity.transit.ElectricalSource

interface ElectricalDevice {
    fun connectPowerSource(source: ElectricalSource)
    fun disconnectPowerSource()
}