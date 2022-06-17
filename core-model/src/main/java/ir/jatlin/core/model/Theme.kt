package ir.jatlin.core.model

enum class Theme(val prefsKey: String) {
    LIGHT("light"),
    DARK("dark"),
    SYSTEM("system"),
    BATTERY_SAVER("battery_saver");

    companion object {
        fun fromPreferenceKey(prefsKey: String): Theme? =
            values().firstOrNull { it.prefsKey == prefsKey }
    }
}