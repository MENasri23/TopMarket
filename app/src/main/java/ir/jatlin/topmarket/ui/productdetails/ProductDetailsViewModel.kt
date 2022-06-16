package ir.jatlin.topmarket.ui.productdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jatlin.topmarket.core.domain.order.GetActiveOrderUseCase
import ir.jatlin.topmarket.core.domain.product.FetchProductDetailsUseCase
import ir.jatlin.topmarket.core.domain.product.FetchProductReviewsUseCase
import ir.jatlin.topmarket.core.domain.product.FetchProductsListStreamUseCase
import ir.jatlin.topmarket.core.domain.purchase.FetchOrderLineItemUseCase
import ir.jatlin.topmarket.core.domain.purchase.UpdateOrderCartUseCase
import ir.jatlin.topmarket.core.domain.util.GetFormattedDateUseCase
import ir.jatlin.topmarket.core.domain.util.makeProductParams
import ir.jatlin.topmarket.core.model.common.ProductImage
import ir.jatlin.topmarket.core.model.order.Order
import ir.jatlin.topmarket.core.model.order.OrderLineItem
import ir.jatlin.topmarket.core.model.product.Product
import ir.jatlin.topmarket.core.model.product.ProductDetails
import ir.jatlin.topmarket.core.shared.Resource
import ir.jatlin.topmarket.core.shared.dataOnSuccessOr
import ir.jatlin.topmarket.core.shared.emptyListResource
import ir.jatlin.topmarket.core.shared.isSuccess
import ir.jatlin.topmarket.ui.util.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val fetchProductDetailsUseCase: FetchProductDetailsUseCase,
    private val fetchProductsListStreamUseCase: FetchProductsListStreamUseCase,
    private val fetchOrderLineItemUseCase: FetchOrderLineItemUseCase,
    private val updateOrderCartUseCase: UpdateOrderCartUseCase,
    private val fetchProductReviewsUseCase: FetchProductReviewsUseCase,
    private val getFormattedDateUseCase: GetFormattedDateUseCase,
    private val getActiveOrderStreamUseCase: GetActiveOrderUseCase,
    state: SavedStateHandle
) : ViewModel() {
    private val productId = state.getLiveData<Int>("productId").asFlow()

//    private var orderLineItem: OrderLineItem? = null

    private val _productDetails = MutableStateFlow<ProductDetails?>(null)
    val productDetails = _productDetails.asStateFlow()

    private val _productImages = MutableStateFlow<List<ProductImage>?>(null)
    val productImages = _productImages.asStateFlow()

    private val _similarProducts: MutableStateFlow<Resource<List<Product>>> =
        MutableStateFlow(Resource.success(emptyList()))
    val similarProducts = _similarProducts.asStateFlow()


    private val _onLoadingQuantity = MutableStateFlow(true)
    val onLoadingQuantity = _onLoadingQuantity.asStateFlow()

    private val _activeOrder = MutableStateFlow<Resource<Order?>>(Resource.loading())
    val activeOrder = _activeOrder.asStateFlow()

    private val orderLineItemParams = productId.combine(activeOrder) { productId, activeOrder ->
        val successOrder = activeOrder.dataOnSuccessOr(null) ?: return@combine null
        FetchOrderLineItemUseCase.Params(successOrder.id, productId)
    }

    val orderLineItem = stateFlow(null) {
        orderLineItemParams.flatMapLatest {
            Timber.d("params:$it")
            fetchOrderLineItemUseCase(it).map { orderLineItemResult ->
                Timber.d("orderlineitemresult: $orderLineItemResult")
                orderLineItemResult.dataOnSuccessOr(null)
            }
        }
    }

    val orderQuantity = stateFlow(0) {
        orderLineItem.map {
            Timber.d("orderLine item : $it")
            _onLoadingQuantity.value = false
            it?.quantity ?: 0
        }
    }


    val productDetailsState = stateFlow {
        productId.map {
            Timber.d("productId: $it")
            fetchProductDetailsUseCase(it)
        }
    }

    val productReviewsState: Flow<Resource<List<ProductReviewItem>>> =
        productDetailsState.map { getProductReviewsItems(it) }


    init {
        fetchActiveOrderStream()
    }


    private fun fetchActiveOrderStream() {
        viewModelScope.launch {
            getActiveOrderStreamUseCase(Unit).collect {
                Timber.d("active order: $it")
                _activeOrder.value = it
            }
        }
    }


    private suspend fun getProductReviewsItems(
        productDetailsResult: Resource<ProductDetails?>
    ): Resource<List<ProductReviewItem>> {
        Timber.d("getProductReviewsItems: $productDetailsResult")
        return if (productDetailsResult.isSuccess) {
            val productId = productDetailsResult.data?.id ?: return emptyListResource()
            when (val result = fetchProductReviewsUseCase(productId)) {
                is Resource.Error -> {
                    Timber.d("Fetching product reviews failed with error: $result")
                    emptyListResource()
                }
                is Resource.Loading -> Resource.loading()
                is Resource.Success -> {
                    val productReviews = result.data ?: return emptyListResource()
                    val reviewItems = productReviews.map {
                        ProductReviewItem(
                            id = it.id,
                            content = it.content,
                            reviewerName = it.reviewerName,
                            createdDate = getFormattedDateUseCase(it.dateCreatedGmt)
                        )
                    }
                    Resource.success(reviewItems)
                }
            }
        } else emptyListResource()
    }


    suspend fun updateUiStatesWith(productDetails: ProductDetails) {
        Timber.d(productDetails.categories.joinToString())
        _productDetails.emit(productDetails)
        _productImages.emit(productDetails.images)
        fetchSimilarProducts(productDetails.relatedIds)
    }

    private fun fetchSimilarProducts(relatedIds: List<Int>) = viewModelScope.launch {
        if (relatedIds.isNotEmpty()) {
            val params = makeProductParams { includeIds = relatedIds }
            fetchProductsListStreamUseCase(params).collect {
                _similarProducts.emit(it)
            }
        }
    }


    fun addToCart() {
        val newOrderLineItem = updateOrderLineItemQuantity(add = true)
        updateCart(newOrderLineItem)
    }

    fun removeFromCart() {
        val newOrderLineItem = updateOrderLineItemQuantity(add = false)
        updateCart(newOrderLineItem)
    }

    private fun updateCart(orderLineItem: OrderLineItem?) {
        orderLineItem ?: return
        _onLoadingQuantity.value = true
        viewModelScope.launch {
            updateOrderCartUseCase(orderLineItem)
        }
    }

    private fun updateOrderLineItemQuantity(add: Boolean): OrderLineItem? {
        val item = orderLineItem.value
        return if (item != null) {
            val quantity = item.quantity + (if (add) 1 else -1)
            item.copy(quantity = quantity)
        } else {
            val product = productDetails.value ?: return null
            OrderLineItem(
                id = 0,
                productId = product.id,
                productName = "",
                totalPrice = "",
                quantity = 1
            )
        }
    }


    fun toggleFavorite() {
        // TODO: toggle the product favorite state
    }
}

data class ProductReviewItem(
    val id: Int,
    val content: String,
    val reviewerName: String,
    val createdDate: String
)