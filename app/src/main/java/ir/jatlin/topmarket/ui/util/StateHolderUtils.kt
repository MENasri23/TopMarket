package ir.jatlin.topmarket.ui.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.jatlin.topmarket.core.shared.Resource
import ir.jatlin.topmarket.core.shared.isSuccess
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

inline fun <T> ViewModel.stateFlow(
    crossinline getValue: () -> Flow<Resource<T>>
): StateFlow<Resource<T>> {
    return getValue().stateIn(
        scope = viewModelScope,
        initialValue = Resource.loading(),
        started = SharingStarted.WhileSubscribed(5000L)
    )
}

inline fun <T> ViewModel.stateFlow(
    initialValue: T,
    crossinline getValue: () -> Flow<T>
): StateFlow<T> {
    return getValue().stateIn(
        scope = viewModelScope,
        initialValue = initialValue,
        started = SharingStarted.WhileSubscribed(5000L)
    )
}

fun Job?.cancelIfAlive() {
    if (this?.isActive == true) cancel()
}

fun <T> allSuccess(vararg resources: Resource<T>): Boolean =
    resources.all { it.isSuccess }

fun <T> findAnyFailed(vararg resources: Resource<T>): Resource.Error? =
    resources.firstOrNull { it is Resource.Error } as? Resource.Error

fun <T> anyLoading(vararg resources: Resource<T>): Boolean =
    resources.any { it is Resource.Loading }