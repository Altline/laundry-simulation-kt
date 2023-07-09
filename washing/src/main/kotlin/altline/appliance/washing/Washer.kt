package altline.appliance.washing

import altline.appliance.common.Body

interface Washer {
    val running: Boolean
    val load: Set<Body>

    fun load(vararg items: Body)
    fun unload(vararg items: Body)
    fun unloadAll(): Set<Body>
    fun start()
    fun stop()
}