package altline.appliance.ui.resources

import java.util.*

val strings: ResourceBundle by lazy { ResourceBundle.getBundle("Strings") }
operator fun ResourceBundle.get(key: String): String = getString(key)