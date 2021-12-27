package altline.things.washing

import altline.things.common.Body

interface Washer {
    fun load(item: Body)
    fun load(items: Collection<Body>)
    fun unload(item: Body)
    fun unload(items: Collection<Body>)
    fun unload(): List<Body>
    fun start()
    fun stop()
}