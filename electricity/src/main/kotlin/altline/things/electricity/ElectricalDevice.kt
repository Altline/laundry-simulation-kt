package altline.things.electricity

interface ElectricalDevice {
    fun connectPowerSource(source: ElectricalConduit)
    fun disconnectPowerSource()
}