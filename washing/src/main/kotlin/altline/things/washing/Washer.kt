package altline.things.washing

import altline.things.common.Body

interface Washer {
    val running: Boolean

    fun load(vararg items: Body)
    fun unload(vararg items: Body)
    fun unloadAll(): List<Body>
    fun start()
    fun stop()
}