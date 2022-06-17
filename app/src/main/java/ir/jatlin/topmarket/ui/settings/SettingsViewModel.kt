package ir.jatlin.topmarket.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jatlin.core.model.Theme
import ir.jatlin.core.shared.Resource
import ir.jatlin.core.shared.dataOnSuccessOr
import ir.jatlin.core.shared.isSuccess
import ir.jatlin.core.shared.theme.ThemeUtils
import ir.jatlin.topmarket.core.domain.service.CancelWorkRequestUseCase
import ir.jatlin.topmarket.core.domain.service.EnqueueFetchNewestProductsWorkRequestUseCase
import ir.jatlin.topmarket.core.domain.settings.*
import ir.jatlin.topmarket.service.sync.FetchNewestProductsWorker
import ir.jatlin.topmarket.ui.util.cancelIfAlive
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.launch
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
            getNotificationEnabledUseCase(Unit)
                .dropWhileFirstLaunched()
                .collect {
                    Timber.d("Notification enabled: $it")
                    if (it.isSuccess) {
                        _notificationEnabled.value = it.dataOnSuccessOr(true)
                        invalidateNewestProductsWorkRequest()
                    }
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
            getNotificationIntervalStreamUseCase(Unit)
                .dropWhileFirstLaunched()
                .collect {
                    if (it.isSuccess) {
                        _notificationInterval.value = it.dataOnSuccessOr(5)
                        if (notificationEnabled.value) {
                            enqueueNewestProductsWorkRequest()
                        }
                    }
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

    private fun enqueueNewestProductsWorkRequest() {
        enqueueWorkJob?.cancelIfAlive()
        enqueueWorkJob = externalScope.launch {
            delay(700L)
            enqueueNewestProductsWorkRequestUseCase(
                notificationInterval.value
            )
        }
    }

    private fun cancelEnqueueNewestProductsWorkRequest() {
        cancelWorkJob?.cancelIfAlive()
        cancelWorkJob = externalScope.launch {
            delay(700L)
            cancelWorkRequestUseCase(FetchNewestProductsWorker.WORKER_NAME)
        }
    }


    private fun <T> Flow<Resource<T>>.dropWhileFirstLaunched(): Flow<Resource<T>> {
        var firstLaunch = true
        return dropWhile {
            val drop = firstLaunch
            if (it is Resource.Success && firstLaunch) {
                firstLaunch = false
            }
            drop
        }
    }


}
