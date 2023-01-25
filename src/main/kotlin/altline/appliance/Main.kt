package altline.appliance

import altline.appliance.audio.Audio
import altline.appliance.di.coreModule
import altline.appliance.ui.MainScreen
import altline.appliance.ui.MainViewModel
import altline.appliance.ui.theme.AppTheme
import androidx.compose.runtime.*
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import java.awt.Dimension

fun main() = application {
    var initialized by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        initialize()
        initialized = true
    }

    Window(
        onCloseRequest = ::exitApplication,
        state = WindowState(
            size = DpSize(1200.dp, 800.dp)
        )
    ) {
        window.minimumSize = Dimension(700, 500)
        if (initialized) {
            AppTheme {
                val viewModel = GlobalContext.get().get<MainViewModel>()
                MainScreen(viewModel)
            }
        }
    }
}

private fun initialize() {
    startKoin {
        modules(coreModule)
    }

    Audio.init()
    Runtime.getRuntime().addShutdownHook(Thread {
        Audio.close()
    })
}