package altline.things.washing.laundry

import altline.things.common.Body
import altline.things.electricity.ElectricalConduit
import altline.things.electricity.ElectricalDevice
import altline.things.washing.Washer

abstract class LaundryWasher(

) : Washer, ElectricalDevice {

    override fun connectPowerSource(source: ElectricalConduit) {
        TODO("Not yet implemented")
    }

    override fun disconnectPowerSource() {
        TODO("Not yet implemented")
    }

    override fun load(item: Body) {
        TODO("Not yet implemented")
    }

    override fun load(items: Collection<Body>) {
        TODO("Not yet implemented")
    }

    override fun unload(item: Body) {
        TODO("Not yet implemented")
    }

    override fun unload(items: Collection<Body>) {
        TODO("Not yet implemented")
    }

    override fun unload(): List<Body> {
        TODO("Not yet implemented")
    }

    override fun start() {
        TODO("Not yet implemented")
    }

    override fun stop() {
        TODO("Not yet implemented")
    }
}