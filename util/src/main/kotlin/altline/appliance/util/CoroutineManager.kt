package altline.appliance.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CoroutineManager(
    private val scope: CoroutineScope,
    private val coroutineBlock: suspend () -> Unit
) {
    private lateinit var job: Job

    private var _active = false
    var active: Boolean
        get() = _active
        set(value) {
            if (value) launch()
            else cancel()
        }

    private fun launch() {
        if (!_active) {
            job = scope.launch { coroutineBlock() }
            _active = true
        }
    }

    private fun cancel() {
        if (_active) {
            _active = false
            job.cancel()
        }
    }
}