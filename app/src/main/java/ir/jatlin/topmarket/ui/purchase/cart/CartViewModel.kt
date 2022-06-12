package ir.jatlin.topmarket.ui.purchase.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jatlin.topmarket.core.domain.order.GetActiveOrderUseCase
import ir.jatlin.topmarket.core.model.order.Order
import ir.jatlin.topmarket.core.shared.dataOnSuccessOr
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getActiveOrderUseCase: GetActiveOrderUseCase,
) : ViewModel() {

    private val _activeOrder = MutableStateFlow<Order?>(null)
    val activeOrder = _activeOrder.asStateFlow()


    init {
        viewModelScope.launch {
            getActiveOrderUseCase(Unit).collect {
                _activeOrder.value = it.dataOnSuccessOr(null)
            }
        }


    }
}