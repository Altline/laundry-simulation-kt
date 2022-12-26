package altline.appliance

import altline.appliance.di.coreModule
import altline.appliance.ui.MainScreen
import altline.appliance.ui.MainViewModel
import altline.appliance.ui.theme.AppTheme
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import java.awt.Dimension

fun main() = application {
    startKoin {
        modules(coreModule)
    }
    Window(
        onCloseRequest = ::exitApplication,
        state = WindowState(
            size = DpSize(1200.dp, 800.dp)
        )
    ) {
        window.minimumSize = Dimension(700, 500)
        AppTheme {
            val viewModel = GlobalContext.get().get<MainViewModel>()
            MainScreen(viewModel)
        }
    }
}
