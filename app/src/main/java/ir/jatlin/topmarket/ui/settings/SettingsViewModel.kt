package ir.jatlin.topmarket.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jatlin.topmarket.core.domain.settings.EnableNotificationUseCase
import ir.jatlin.topmarket.core.domain.settings.GetNotificationEnabledUseCase
import ir.jatlin.topmarket.core.domain.settings.GetThemeStreamUseCase
import ir.jatlin.topmarket.core.domain.settings.SetThemeUseCase
import ir.jatlin.topmarket.core.model.Theme
import ir.jatlin.topmarket.core.shared.dataOnSuccessOr
import ir.jatlin.topmarket.core.shared.theme.ThemeUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val enableNotificationUseCase: EnableNotificationUseCase,
    getNotificationEnabledUseCase: GetNotificationEnabledUseCase,
    private val setThemeUseCase: SetThemeUseCase,
    getThemeStreamUseCase: GetThemeStreamUseCase
) : ViewModel() {

    private val _notificationEnabled = MutableStateFlow(true)
    val notificationEnabled = _notificationEnabled.asStateFlow()

    private val _darkThemeEnabled = MutableStateFlow(false)
    val darkThemeEnabled = _darkThemeEnabled.asStateFlow()

    private val _defaultSystemTheme = MutableStateFlow(false)
    val defaultSystemTheme = _defaultSystemTheme.asStateFlow()

    init {
        viewModelScope.launch {
            getNotificationEnabledUseCase(Unit).collect {
                Timber.d("Notification enabled: $it")
                _notificationEnabled.value = it.dataOnSuccessOr(true)
            }
        }

        viewModelScope.launch {
            getThemeStreamUseCase(Unit).collect {
                Timber.d("dark theme enabled: $it")
                val theme = it.dataOnSuccessOr(ThemeUtils.defaultTheme())
                _defaultSystemTheme.value = theme == Theme.SYSTEM || theme == Theme.BATTERY_SAVER
                _darkThemeEnabled.value = theme == Theme.DARK
            }
        }


    }


    fun enableNotification(isEnabled: Boolean) {
        viewModelScope.launch {
            enableNotificationUseCase(isEnabled)
        }
    }

    fun setDarkTheme(isEnabled: Boolean) {
        viewModelScope.launch {
            setThemeUseCase(if (isEnabled) Theme.DARK else Theme.LIGHT)
        }
    }

    fun setDefaultSystemTheme(isEnabled: Boolean) {
        viewModelScope.launch {
            setThemeUseCase(
                when {
                    isEnabled -> ThemeUtils.defaultTheme()
                    darkThemeEnabled.value -> Theme.DARK
                    else -> Theme.LIGHT
                }
            )
        }
    }


}
