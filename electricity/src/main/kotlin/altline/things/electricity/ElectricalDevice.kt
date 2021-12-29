package altline.things.electricity

interface ElectricalDevice {
    fun connectPowerSource(source: ElectricalSource)
    fun disconnectPowerSource()
}