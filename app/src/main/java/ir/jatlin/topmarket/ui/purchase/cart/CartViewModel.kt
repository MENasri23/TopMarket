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
import ir.jatlin.topmarket.ui.util.processResult
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
    val activeOrder = _activeOrder.asStateFlow()

    private val _coupon = MutableStateFlow<Coupon?>(null)
    val coupon = _coupon.asStateFlow()

    private val _discountExpanded = MutableStateFlow(false)
    val discountExpanded = _discountExpanded.asStateFlow()

    val cartProductItems = activeOrder.map {
        it?.let {
            startLoading()
            CartProductsItem(
                fetchCartProductListUseCase(it.orderItems)
                    .dataOnSuccessOr(null)
            ).also { stopLoading() }

        }
    }


    val orderItemsCount = stateFlow(0) {
        activeOrder.map { order ->
            order?.orderItems?.sumOf { it.quantity }
        }
    }

    val sumOfDiscounts = stateFlow(null) {
        cartProductItems.map { cartProductItems ->
            val products = cartProductItems?.products ?: return@map null

            val sum = products.sumOf { it.regularPrice - it.totalPrice }
            sum.takeIf { it > 0 }?.toString()
        }
    }


    val totalPrice = stateFlow(initialValue = null) {
        activeOrder.combine(coupon) { order, coupon ->
            if (order == null) return@combine null
            val totalPrice = calculatePrice(order.totalPrice, coupon?.amount)
            String.format("%.0f", totalPrice)
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

    private fun calculatePrice(regularPrice: String, discount: String?): Double {
        return try {
            val price = regularPrice.toLong()
            if (discount != null) {
                floor((1 - discount.toDouble() / 100) * price)
            } else price.toDouble()
        } catch (e: NumberFormatException) {
            Timber.d("The total price can't be obtained from price:$regularPrice, discount:$discount")
            0.0
        }

    }

    private fun processActiveOrderResult(result: Resource<Order?>) {
        Timber.d("$result")
        when (result) {
            is Resource.Error -> {
                stopLoading()
                _errorCause.value = result.cause
            }
            is Resource.Loading -> startLoading()
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

    private fun processCouponResult(result: Resource<Coupon?>) {
        Timber.d("coupon: $coupon")
        result.processResult(
            onError = { cause -> _errorCause.value = cause },
            onLoading = { }
        ) {
            _coupon.value = it
        }
    }

    private fun startLoading() {
        _loading.value = true
    }

    private fun stopLoading() {
        _loading.value = false
    }


    fun checkCouponCode(code: String?) {
        Timber.d("coupon code: $code")
        if (code.isNullOrBlank()) {
            return
        }
        viewModelScope.launch {
            val coupon = getCouponByCodeUseCase(code.trim())
            processCouponResult(coupon)
        }
    }


    fun onToggleDiscountExpand() {
        _discountExpanded.value = !discountExpanded.value
    }
}

data class CartProductsItem(
    val products: List<CartProduct>?
)