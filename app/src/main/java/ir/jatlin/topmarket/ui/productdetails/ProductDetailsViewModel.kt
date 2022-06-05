package ir.jatlin.topmarket.ui.productdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jatlin.topmarket.core.domain.product.FetchProductDetailsUseCase
import ir.jatlin.topmarket.core.domain.product.FetchProductsListUseCase
import ir.jatlin.topmarket.core.domain.purchase.FetchOrderLineItemUseCase
import ir.jatlin.topmarket.core.domain.purchase.UpdateOrderCartUseCase
import ir.jatlin.topmarket.core.domain.util.makeProductParams
import ir.jatlin.topmarket.core.model.order.OrderLineItem
import ir.jatlin.topmarket.core.network.model.common.NetworkImage
import ir.jatlin.topmarket.core.network.model.product.NetworkProduct
import ir.jatlin.topmarket.core.network.model.product.NetworkProductDetails
import ir.jatlin.topmarket.core.shared.Resource
import ir.jatlin.topmarket.core.shared.isSuccess
import ir.jatlin.topmarket.ui.util.stateFlow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val fetchProductDetailsUseCase: FetchProductDetailsUseCase,
    private val fetchProductsListUseCase: FetchProductsListUseCase,
    private val fetchOrderLineItemUseCase: FetchOrderLineItemUseCase,
    private val updateOrderCartUseCase: UpdateOrderCartUseCase,
    state: SavedStateHandle
) : ViewModel() {
    private val productId = state.getLiveData<Int>("productId").asFlow()

    private val _orderLineItem = MutableStateFlow<Resource<OrderLineItem?>>(Resource.loading())
    val orderLineItem = _orderLineItem.asStateFlow()

    init {
        fetchRelatedOrderLineItem()
    }

    private val _productDetails = MutableStateFlow<NetworkProductDetails?>(null)
    val productDetails = _productDetails.asStateFlow()

    private val _productImages = MutableStateFlow<List<NetworkImage>?>(null)
    val productImages = _productImages.asStateFlow()

    private val _similarProducts: MutableStateFlow<Resource<List<NetworkProduct>>> =
        MutableStateFlow(Resource.success(emptyList()))
    val similarProducts = _similarProducts.asStateFlow()


    private val _orderQuantity = MutableStateFlow(0)
    val orderQuantity = _orderQuantity.asStateFlow()

    val productDetailsState = stateFlow {
        productId.map {
            Timber.d("productId: $it")
            fetchProductDetailsUseCase(it)
        }
    }


    private fun fetchRelatedOrderLineItem() {
        viewModelScope.launch {
            productId.collect { productId ->
                fetchOrderLineItemUseCase(productId).collectLatest {
                    Timber.d("$it")
                    _orderLineItem.value = it
                    if (it.isSuccess) {
                        _orderQuantity.value = it.data!!.quantity
                    }
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
            fetchProductsListUseCase(params).collect {
                _similarProducts.emit(it)
            }
        }
    }


    fun addToCart() {
        val itemResource = orderLineItem.value
        val orderLineItem = if (itemResource.isSuccess) {
            val oldOrderLineItem = itemResource.data!!
            oldOrderLineItem.copy(quantity = oldOrderLineItem.quantity + 1)
        } else {
            val product = productDetails.value ?: return
            OrderLineItem(
                id = 0,
                productId = product.id,
                productName = "",
                totalPrice = "",
                quantity = 1
            )
        }
        viewModelScope.launch {
            updateOrderCartUseCase(orderLineItem).collect {
                Timber.d("$it")
            }
        }
    }


    fun toggleFavorite() {

    }
}