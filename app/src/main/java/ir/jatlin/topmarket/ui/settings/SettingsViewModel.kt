package ir.jatlin.topmarket.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jatlin.topmarket.core.domain.settings.*
import ir.jatlin.topmarket.core.model.Theme
import ir.jatlin.topmarket.core.shared.dataOnSuccessOr
import ir.jatlin.topmarket.core.shared.theme.ThemeUtils
import ir.jatlin.topmarket.ui.util.cancelIfAlive
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val enableNotificationUseCase: EnableNotificationUseCase,
    getNotificationEnabledUseCase: GetNotificationEnabledUseCase,
    private val setThemeUseCase: SetThemeUseCase,
    getThemeStreamUseCase: GetThemeStreamUseCase,
    private val setNotificationInterval: SetNotificationIntervalUseCase,
    getNotificationIntervalStreamUseCase: GetNotificationIntervalStreamUseCase,
    getPreSetNotificationIntervals: GetPreSetNotificationIntervals
) : ViewModel() {

    private val _notificationEnabled = MutableStateFlow(true)
    val notificationEnabled = _notificationEnabled.asStateFlow()

    private val _darkThemeEnabled = MutableStateFlow(false)
    val darkThemeEnabled = _darkThemeEnabled.asStateFlow()

    private val _defaultSystemTheme = MutableStateFlow(false)
    val defaultSystemTheme = _defaultSystemTheme.asStateFlow()

    private val _notificationInterval = MutableStateFlow(5)
    val notificationInterval = _notificationInterval.asStateFlow()

    private val _preSetNotificationIntervals = MutableStateFlow<List<Int>>(emptyList())
    val preSetNotificationIntervals = _preSetNotificationIntervals.asStateFlow()

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
                val systemTheme = theme == Theme.SYSTEM || theme == Theme.BATTERY_SAVER
                _defaultSystemTheme.value = systemTheme
                if (!systemTheme) {
                    _darkThemeEnabled.value = theme == Theme.DARK
                }
            }
        }

        viewModelScope.launch {
            getNotificationIntervalStreamUseCase(Unit).collect {
                _notificationInterval.value = it.dataOnSuccessOr(5)
            }
        }

        viewModelScope.launch {
            _preSetNotificationIntervals.value =
                getPreSetNotificationIntervals(Unit).dataOnSuccessOr(emptyList())
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
            delay(100)
            setThemeUseCase(
                when {
                    isEnabled -> ThemeUtils.defaultTheme()
                    darkThemeEnabled.value -> Theme.DARK
                    else -> Theme.LIGHT
                }
            )
        }
    }

    fun setInterval(interval: Int) {
        viewModelScope.launch {
            setNotificationInterval(interval)
        }
    }


}
