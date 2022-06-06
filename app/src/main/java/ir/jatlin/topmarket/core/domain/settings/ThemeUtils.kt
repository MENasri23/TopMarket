package ir.jatlin.topmarket.core.domain.settings

import android.os.Build
import ir.jatlin.topmarket.core.model.Theme

object ThemeUtils {

    fun defaultTheme() = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> Theme.SYSTEM
        else -> Theme.BATTERY_SAVER
    }
}