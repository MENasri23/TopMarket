package ir.jatlin.topmarket.ui.purchase.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jatlin.core.model.coupon.Coupon
import ir.jatlin.core.model.order.Order
import ir.jatlin.core.model.order.OrderLineItem
import ir.jatlin.core.model.product.CartProduct
import ir.jatlin.core.shared.Resource
import ir.jatlin.core.shared.dataOnSuccessOr
import ir.jatlin.core.shared.fail.ErrorCause
import ir.jatlin.topmarket.core.domain.coupon.GetCouponByCodeUseCase
import ir.jatlin.topmarket.core.domain.order.GetActiveOrderStreamUseCase
import ir.jatlin.topmarket.core.domain.purchase.GetCartProductListUseCase
import ir.jatlin.topmarket.core.domain.purchase.UpdateOrderCartUseCase
import ir.jatlin.topmarket.ui.util.cancelIfAlive
import ir.jatlin.topmarket.ui.util.processResult
import ir.jatlin.topmarket.ui.util.stateFlow
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.floor

@HiltViewModel
class CartViewModel @Inject constructor(
    private val getActiveOrderStreamUseCase: GetActiveOrderStreamUseCase,
    private val fetchCartProductListUseCase: GetCartProductListUseCase,
    private val getCouponByCodeUseCase: GetCouponByCodeUseCase,
    private val updateOrderCartUseCase: UpdateOrderCartUseCase,

    ) : ViewModel() {

    private var updateOrderJob: Job? = null
    private var activeOrderJob: Job? = null

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

    private val _cartItemLoadingState = MutableStateFlow<Unit?>(null)
    val cartItemLoadingState = _cartItemLoadingState.asStateFlow()

    var cartItemLoadingPosition: Int? = null
        private set


    val cartProductItems = stateFlow(CartProductsItem(emptyList())) {
        activeOrder.map(this::toCartProductItem)
    }

    private val _orderId = MutableStateFlow<Int?>(null)
    val orderId = _orderId.asStateFlow()

    private suspend fun toCartProductItem(order: Order?): CartProductsItem? {
        if (order == null) return null
        startLoading()
        return CartProductsItem(
            fetchCartProductListUseCase(order.orderItems)
                .dataOnSuccessOr(null)
        )
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


    fun fetchActiveOrder() {
        activeOrderJob = viewModelScope.launch {
            getActiveOrderStreamUseCase(Unit).collectLatest {
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
                stopLoading()
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

    fun startLoading() {
        if (noNestedLoading()) {
            _loading.value = true
        }
    }

    fun stopLoading() {
        _loading.value = false
    }

    private fun getUpdatedOrderLineItem(cartProduct: CartProduct, add: Boolean): OrderLineItem {
        return OrderLineItem.Empty.copy(
            id = cartProduct.orderLineId,
            productId = cartProduct.productId,
            quantity = cartProduct.quantity + (if (add) 1 else -1)
        )
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

    fun addToCart(cartProduct: CartProduct, position: Int) {
        cartItemLoadingPosition = position
        _cartItemLoadingState.value = Unit

        updateOrderJob.cancelIfAlive()
        val orderLineItem = getUpdatedOrderLineItem(cartProduct, true)
        updateOrderJob = viewModelScope.launch {
            delay(500L)
            updateOrderCartUseCase(orderLineItem)
        }
    }

    fun removeFromCart(cartProduct: CartProduct, position: Int) {
        cartItemLoadingPosition = position
        _cartItemLoadingState.value = Unit

        updateOrderJob.cancelIfAlive()
        val orderLineItem = getUpdatedOrderLineItem(cartProduct, false)
        updateOrderJob = viewModelScope.launch {
            delay(500L)
            updateOrderCartUseCase(orderLineItem)
        }
    }

    fun onCartItemLoadingCompleted() {
        cartItemLoadingPosition = null
        _cartItemLoadingState.value = null
    }

    fun noNestedLoading(): Boolean {
        return cartItemLoadingState.value == null
    }

    fun onNavigateToShippingScreen() {
        _orderId.value = activeOrder.value?.id
    }

    fun onNavigateToShippingScreenCompleted() {
        _activeOrder.value = null
        _orderId.value = null
    }

    fun clearCache() {
        activeOrderJob.cancelIfAlive()
    }
}

data class CartProductsItem(
    val products: List<CartProduct>?
)