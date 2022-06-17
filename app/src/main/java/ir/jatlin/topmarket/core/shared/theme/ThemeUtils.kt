package ir.jatlin.topmarket.core.shared.theme

import android.os.Build
import ir.jatlin.core.model.Theme

object ThemeUtils {

    fun defaultTheme() = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> Theme.SYSTEM
        else -> Theme.BATTERY_SAVER
    }
}