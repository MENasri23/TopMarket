package ir.jatlin.topmarket.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jatlin.topmarket.core.domain.service.CancelWorkRequestUseCase
import ir.jatlin.topmarket.core.domain.service.EnqueueFetchNewestProductsWorkRequestUseCase
import ir.jatlin.topmarket.core.domain.settings.*
import ir.jatlin.topmarket.core.model.Theme
import ir.jatlin.topmarket.core.shared.dataOnSuccessOr
import ir.jatlin.topmarket.core.shared.theme.ThemeUtils
import ir.jatlin.topmarket.service.sync.FetchNewestProductsWorker
import ir.jatlin.topmarket.ui.util.cancelIfAlive
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val externalScope: CoroutineScope,
    private val enableNotificationUseCase: EnableNotificationUseCase,
    getNotificationEnabledUseCase: GetNotificationEnabledStreamUseCase,
    private val setThemeUseCase: SetThemeUseCase,
    getThemeStreamUseCase: GetThemeStreamUseCase,
    private val setNotificationInterval: SetNotificationIntervalUseCase,
    getNotificationIntervalStreamUseCase: GetNotificationIntervalStreamUseCase,
    getPreSetNotificationIntervals: GetPreSetNotificationIntervals,
    private val cancelWorkRequestUseCase: CancelWorkRequestUseCase,
    private val enqueueNewestProductsWorkRequestUseCase: EnqueueFetchNewestProductsWorkRequestUseCase
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

    private var enqueueWorkJob: Job? = null
    private var cancelWorkJob: Job? = null


    init {
        viewModelScope.launch {
            getNotificationEnabledUseCase(Unit).collect {
                Timber.d("Notification enabled: $it")
                _notificationEnabled.value = it.dataOnSuccessOr(true)
            }
        }

        viewModelScope.launch {
            getThemeStreamUseCase(Unit).collect {
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

        viewModelScope.launch {
            getNotificationEnabledUseCase(Unit).collectLatest {
                _notificationEnabled.value = it.dataOnSuccessOr(true)
                invalidateNewestProductsWorkRequest()
            }
        }

        viewModelScope.launch {
            getNotificationIntervalStreamUseCase(Unit).collectLatest {
                _notificationInterval.value = it.dataOnSuccessOr(5)
                if (notificationEnabled.value) enqueueNewestProductsWorkRequest()
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
            delay(200L)
            setThemeUseCase(if (isEnabled) Theme.DARK else Theme.LIGHT)
        }
    }

    fun setDefaultSystemTheme(isEnabled: Boolean) {
        viewModelScope.launch {
            delay(100L)
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
        if (interval <= 0) return

        viewModelScope.launch {
            setNotificationInterval(interval)
        }
    }

    private fun invalidateNewestProductsWorkRequest() {
        if (notificationEnabled.value) enqueueNewestProductsWorkRequest()
        else cancelEnqueueNewestProductsWorkRequest()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun enqueueNewestProductsWorkRequest() {
        enqueueWorkJob?.cancelIfAlive()
        enqueueWorkJob = externalScope.launch {
            delay(700L)
            enqueueNewestProductsWorkRequestUseCase(
                notificationInterval.value
            )
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun cancelEnqueueNewestProductsWorkRequest() {
        cancelWorkJob?.cancelIfAlive()
        cancelWorkJob = externalScope.launch {
            delay(700L)
            cancelWorkRequestUseCase(FetchNewestProductsWorker.WORKER_NAME)
        }
    }


}
