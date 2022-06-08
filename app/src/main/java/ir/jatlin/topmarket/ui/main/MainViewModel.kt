package ir.jatlin.topmarket.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.jatlin.topmarket.core.domain.service.CancelWorkRequestUseCase
import ir.jatlin.topmarket.core.domain.service.EnqueueFetchNewestProductsWorkRequestUseCase
import ir.jatlin.topmarket.core.domain.settings.GetNotificationEnabledStreamUseCase
import ir.jatlin.topmarket.core.domain.settings.GetNotificationIntervalStreamUseCase
import ir.jatlin.topmarket.core.shared.dataOnSuccessOr
import ir.jatlin.topmarket.service.sync.FetchNewestProductsWorker
import ir.jatlin.topmarket.ui.util.cancelIfAlive
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val enqueueNewestProductsWorkRequestUseCase: EnqueueFetchNewestProductsWorkRequestUseCase,
    private val cancelWorkRequestUseCase: CancelWorkRequestUseCase,
    getNotificationEnabledStreamUseCase: GetNotificationEnabledStreamUseCase,
    getNotificationIntervalStreamUseCase: GetNotificationIntervalStreamUseCase,
) : ViewModel() {

    private val _notificationEnabled = MutableStateFlow(true)
    private val notificationEnabled = _notificationEnabled.asStateFlow()

    private val _notificationInterval = MutableStateFlow(5)
    private val notificationInterval = _notificationInterval.asStateFlow()

    private var enqueueWorkJob: Job? = null
    private var cancelWorkJob: Job? = null


    init {
        viewModelScope.launch {
            getNotificationEnabledStreamUseCase(Unit).collectLatest {
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

    private fun invalidateNewestProductsWorkRequest() {
        if (notificationEnabled.value) enqueueNewestProductsWorkRequest()
        else cancelEnqueueNewestProductsWorkRequest()
    }

    private fun enqueueNewestProductsWorkRequest() {
        enqueueWorkJob?.cancelIfAlive()
        enqueueWorkJob = viewModelScope.launch {
            delay(700L)
            enqueueNewestProductsWorkRequestUseCase(
                notificationInterval.value
            )
        }
    }

    private fun cancelEnqueueNewestProductsWorkRequest() {
        cancelWorkJob?.cancelIfAlive()
        cancelWorkJob = viewModelScope.launch {
            delay(700L)
            cancelWorkRequestUseCase(FetchNewestProductsWorker.WORKER_NAME)
        }
    }


}