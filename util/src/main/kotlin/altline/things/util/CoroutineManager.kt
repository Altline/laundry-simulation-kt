package altline.things.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class CoroutineManager(
    val scope: CoroutineScope,
    private val coroutineBlock: suspend () -> Unit
) {
    private var _active = false
    var active: Boolean
        get() = _active
        set(value) {
            if (value) launch()
            else cancel()
        }

    private fun launch() {
        if (!_active) {
            _active = true
            scope.launch { coroutineBlock() }
        }
    }

    private fun cancel() {
        if (_active) {
            _active = false
            scope.cancel()
        }
    }
}