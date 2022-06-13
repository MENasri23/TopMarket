package ir.jatlin.topmarket.ui.purchase.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jatlin.topmarket.core.domain.coupon.GetCouponByCodeUseCase
import ir.jatlin.topmarket.core.domain.order.GetActiveOrderStreamUseCase
import ir.jatlin.topmarket.core.domain.purchase.GetCartProductListUseCase
import ir.jatlin.topmarket.core.model.coupon.Coupon
import ir.jatlin.topmarket.core.model.order.Order
import ir.jatlin.topmarket.core.model.product.CartProduct
import ir.jatlin.topmarket.core.shared.Resource
import ir.jatlin.topmarket.core.shared.dataOnSuccessOr
import ir.jatlin.topmarket.core.shared.fail.ErrorCause
import ir.jatlin.topmarket.ui.util.stateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.floor

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getActiveOrderStreamUseCase: GetActiveOrderStreamUseCase,
    private val fetchCartProductListUseCase: GetCartProductListUseCase,
    private val getCouponByCodeUseCase: GetCouponByCodeUseCase
) : ViewModel() {

    private val _loading = MutableStateFlow(true)
    val loading = _loading.asStateFlow()

    private val _errorCause = MutableStateFlow<ErrorCause?>(null)
    val errorCause = _errorCause.asStateFlow()

    private val _discountMessage = MutableStateFlow<Int?>(null)
    val discountMessage = _discountMessage.asStateFlow()

    private val _activeOrder = MutableStateFlow<Order?>(null)
    private val activeOrder = _activeOrder.asStateFlow()

    private val _coupon = MutableStateFlow<Coupon?>(null)
    val coupon = _coupon.asStateFlow()

    val cartProductItems = stateFlow(null) {
        activeOrder.map {
            it?.let {
                CartProductItems(
                    fetchCartProductListUseCase(it.orderItems)
                        .dataOnSuccessOr(null)
                )
            }
        }
    }

    val totalPrice = stateFlow(initialValue = null) {
        activeOrder.combine(coupon) { order, coupon ->
            if (order == null || coupon == null) return@combine null
            val totalPrice = calculatePrice(order.totalPrice, coupon.amount)
            totalPrice.takeIf { it > 0.0 }
        }
    }

    init {
        fetchActiveOrder()
    }

    private fun fetchActiveOrder() {
        viewModelScope.launch {
            getActiveOrderStreamUseCase(Unit).collect {
                processActiveOrderResult(it)
            }
        }
    }

    private fun calculatePrice(regularPrice: String, discount: String): Double {
        return try {
            val price = regularPrice.toLong()
            floor((1 - discount.toDouble() / 100) * price)
        } catch (e: NumberFormatException) {
            Timber.d("The total price can't be obtained from price:$regularPrice, discount:$discount")
            0.0
        }

    }

    private fun processActiveOrderResult(result: Resource<Order?>) {
        when (result) {
            is Resource.Error -> _errorCause.value = result.cause
            is Resource.Loading -> _loading.value = true
            is Resource.Success -> {
                val order = result.data
                if (order == null) {
                    _errorCause.value = ErrorCause.NoContent
                    return
                }
                _activeOrder.value = order
            }
        }
    }


    fun onApplyDiscount(code: String) {
        if (code.isBlank()) {
            return
        }
        viewModelScope.launch {
            _coupon.emit(getCouponByCodeUseCase(code).dataOnSuccessOr(null))
        }
    }
}

data class CartProductItems(
    val products: List<CartProduct>?
)