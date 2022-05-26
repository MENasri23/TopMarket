package ir.jatlin.topmarket.ui.productdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jatlin.topmarket.core.domain.product.FetchProductDetailsUseCase
import ir.jatlin.topmarket.core.domain.product.FetchProductsListUseCase
import ir.jatlin.topmarket.core.domain.util.makeProductParams
import ir.jatlin.topmarket.core.network.model.common.NetworkImage
import ir.jatlin.topmarket.core.network.model.product.NetworkProduct
import ir.jatlin.topmarket.core.network.model.product.NetworkProductDetails
import ir.jatlin.topmarket.core.shared.Resource
import ir.jatlin.topmarket.ui.util.stateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val fetchProductDetailsUseCase: FetchProductDetailsUseCase,
    private val fetchProductsListUseCase: FetchProductsListUseCase,
    state: SavedStateHandle
) : ViewModel() {
    private val productId = state.getLiveData<Int>("productId").asFlow()

    private val _productDetails = MutableStateFlow<NetworkProductDetails?>(null)
    val productDetails = _productDetails.asStateFlow()

    private val _productImages = MutableStateFlow<List<NetworkImage>?>(null)
    val productImages = _productImages.asStateFlow()

    private val _similarProducts: MutableStateFlow<Resource<List<NetworkProduct>>> =
        MutableStateFlow(Resource.success(emptyList()))
    val similarProducts = _similarProducts.asStateFlow()

    private val _addToCartCount = MutableStateFlow(0)
    val addToCartCount = _addToCartCount.asStateFlow()

    val productDetailsState = stateFlow {
        productId.map {
            Timber.d("productId: $it")
            fetchProductDetailsUseCase(it)
        }
    }


    suspend fun updateUiStatesWith(productDetails: NetworkProductDetails) {
        Timber.d("\n\n\n\n${productDetails.categories.joinToString()}\n\n\n\n")
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
        _addToCartCount.value++
    }


    fun toggleFavorite() {

    }
}