package ir.jatlin.topmarket.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jatlin.core.shared.dataOnSuccessOr
import ir.jatlin.topmarket.core.domain.service.EnqueueFetchNewestProductsWorkRequestUseCase
import ir.jatlin.topmarket.core.domain.settings.GetNotificationEnabledStreamUseCase
import ir.jatlin.topmarket.core.domain.settings.GetNotificationIntervalStreamUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val externalScope: CoroutineScope,
    private val enqueueNewestProductsWorkRequestUseCase: EnqueueFetchNewestProductsWorkRequestUseCase,
    getNotificationEnabledStreamUseCase: GetNotificationEnabledStreamUseCase,
    getNotificationIntervalStreamUseCase: GetNotificationIntervalStreamUseCase,
    marketNetworkManager: MarketNetworkManager,
) : ViewModel(), MarketNetworkManager by marketNetworkManager {

    private val _notificationEnabled = MutableStateFlow(true)
    private val notificationEnabled = _notificationEnabled.asStateFlow()

    private val _notificationInterval = MutableStateFlow(5)
    private val notificationInterval = _notificationInterval.asStateFlow()

    private var notifIntervalJob: Job? = null
    private var notifEnabledJob: Job? = null

    init {
        notifEnabledJob = viewModelScope.launch {
            _notificationEnabled.value =
                getNotificationEnabledStreamUseCase(Unit)
                    .firstOrNull().dataOnSuccessOr(true)

        }
        notifIntervalJob = viewModelScope.launch {
            _notificationInterval.value = getNotificationIntervalStreamUseCase(Unit)
                .firstOrNull().dataOnSuccessOr(5)
        }
    }


    fun enqueueNewestProductsWorkRequest() {
        externalScope.launch {
            notifIntervalJob?.join()
            notifEnabledJob?.join()

            if (notificationEnabled.value) {
                enqueueNewestProductsWorkRequestUseCase(
                    notificationInterval.value
                )
            }
        }
    }


}