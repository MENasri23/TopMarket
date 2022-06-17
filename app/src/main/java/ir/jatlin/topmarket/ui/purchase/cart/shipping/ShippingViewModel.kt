package ir.jatlin.topmarket.ui.purchase.cart.shipping

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jatlin.core.model.order.Order
import ir.jatlin.core.model.order.OrderStatus
import ir.jatlin.core.shared.Resource
import ir.jatlin.core.shared.dataOnSuccessOr
import ir.jatlin.core.shared.fail.ErrorCause
import ir.jatlin.topmarket.core.domain.order.MarkOrderCompletedUseCase
import ir.jatlin.topmarket.core.domain.signin.GetCurrentCustomerIdStreamUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShippingViewModel @Inject constructor(
    getCurrentCustomerIdStreamUseCase: GetCurrentCustomerIdStreamUseCase,
    private val markOrderCompletedUseCase: MarkOrderCompletedUseCase
) : ViewModel() {

    val customerId = getCurrentCustomerIdStreamUseCase(Unit)

    private val _error = MutableStateFlow<ErrorCause?>(null)
    val error = _error.asStateFlow()

    private val _loading = MutableStateFlow(true)
    val loading = _loading.asStateFlow()

    private val _order = MutableStateFlow<Order?>(null)
    val order = _order.asStateFlow()

    val orderCompleted = order.map {
        it != null && it.status == OrderStatus.Completed
    }

    val isSignedIn = customerId
        .dropWhile { it is Resource.Loading<*> }
        .map {
            it.dataOnSuccessOr(null) != null
        }


    fun markAsCompleted(orderId: Int) {
        viewModelScope.launch {
            val result = markOrderCompletedUseCase(orderId)
            processOrderResult(result)
        }
    }

    private fun processOrderResult(result: Resource<Order>) {
        when (result) {
            is Resource.Loading -> _loading.value = true
            is Resource.Error -> {
                _loading.value = false
                _error.value = result.cause
            }
            is Resource.Success -> {
                if (result.data != null) {
                    _order.value = result.data
                } else {
                    _error.value = ErrorCause.Unknown()
                }
            }
        }
    }

    fun onShowErrorCompleted() {
        _error.value = null
    }


}