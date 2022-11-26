package altline.appliance

import altline.appliance.di.coreModule
import altline.appliance.ui.MainScreen
import altline.appliance.ui.MainViewModel
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin

fun main() = application {
    startKoin {
        modules(coreModule)
    }
    Window(onCloseRequest = ::exitApplication) {
        val viewModel = GlobalContext.get().get<MainViewModel>()
        MainScreen(viewModel)
    }
}
