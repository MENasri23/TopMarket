package ir.jatlin.topmarket.ui.productdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jatlin.topmarket.core.domain.product.FetchProductDetailsUseCase
import ir.jatlin.topmarket.core.domain.product.FetchProductReviewsUseCase
import ir.jatlin.topmarket.core.domain.product.FetchProductsListStreamUseCase
import ir.jatlin.topmarket.core.domain.purchase.FetchOrderLineItemUseCase
import ir.jatlin.topmarket.core.domain.purchase.UpdateOrderCartUseCase
import ir.jatlin.topmarket.core.domain.util.GetFormattedDateUseCase
import ir.jatlin.topmarket.core.domain.util.makeProductParams
import ir.jatlin.topmarket.core.model.order.OrderLineItem
import ir.jatlin.topmarket.core.network.model.common.NetworkImage
import ir.jatlin.topmarket.core.network.model.product.NetworkProduct
import ir.jatlin.topmarket.core.network.model.product.NetworkProductDetails
import ir.jatlin.topmarket.core.shared.*
import ir.jatlin.topmarket.core.shared.fail.ErrorCause
import ir.jatlin.topmarket.ui.util.safeCollect
import ir.jatlin.topmarket.ui.util.stateFlow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.NullPointerException
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val fetchProductDetailsUseCase: FetchProductDetailsUseCase,
    private val fetchProductsListStreamUseCase: FetchProductsListStreamUseCase,
    private val fetchOrderLineItemUseCase: FetchOrderLineItemUseCase,
    private val updateOrderCartUseCase: UpdateOrderCartUseCase,
    private val fetchProductReviewsUseCase: FetchProductReviewsUseCase,
    private val getFormattedDateUseCase: GetFormattedDateUseCase,
    state: SavedStateHandle
) : ViewModel() {
    private val productId = state.getLiveData<Int>("productId").asFlow()

    private var orderLineItem: OrderLineItem? = null

    private val _productDetails = MutableStateFlow<NetworkProductDetails?>(null)
    val productDetails = _productDetails.asStateFlow()

    private val _productImages = MutableStateFlow<List<NetworkImage>?>(null)
    val productImages = _productImages.asStateFlow()

    private val _similarProducts: MutableStateFlow<Resource<List<NetworkProduct>>> =
        MutableStateFlow(Resource.success(emptyList()))
    val similarProducts = _similarProducts.asStateFlow()


    private val _orderQuantity = MutableStateFlow(0)
    val orderQuantity = _orderQuantity.asStateFlow()

    private val _onLoadingQuantity = MutableStateFlow(true)
    val onLoadingQuantity = _onLoadingQuantity.asStateFlow()

    val productDetailsState = stateFlow {
        productId.map {
            Timber.d("productId: $it")
            fetchProductDetailsUseCase(it)
        }
    }

    val productReviewsState: Flow<Resource<List<ProductReviewItem>>> =
        productDetailsState.map { getProductReviewsItems(it) }

    private suspend fun getProductReviewsItems(
        productDetailsResult: Resource<NetworkProductDetails?>
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


    init {
        fetchRelatedOrderLineItem()
    }


    private fun fetchRelatedOrderLineItem() {
        viewModelScope.launch {
            productId.collect { productId ->
                fetchOrderLineItemUseCase(productId).safeCollect(
                    onFailure = {
                        if (it is ErrorCause.Unknown && it.throwable is NullPointerException) {
                            orderLineItem = null
                        }
                        _onLoadingQuantity.value = false
                        _orderQuantity.value = 0
                    },
                    onLoading = { _onLoadingQuantity.value = true }
                ) {
                    Timber.d("$it")
                    orderLineItem = it
                    _onLoadingQuantity.value = false
                    _orderQuantity.value = it?.quantity ?: 0

                }
            }
        }
    }

    suspend fun updateUiStatesWith(productDetails: NetworkProductDetails) {
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
            updateOrderCartUseCase(orderLineItem).collect {
                Timber.d("$it")
            }
        }
    }

    private fun updateOrderLineItemQuantity(add: Boolean): OrderLineItem? {
        val item = orderLineItem
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

    }
}

data class ProductReviewItem(
    val id: Int,
    val content: String,
    val reviewerName: String,
    val createdDate: String
)