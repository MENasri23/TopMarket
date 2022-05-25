package ir.jatlin.topmarket.ui.productdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jatlin.topmarket.core.domain.product.FetchProductDetailsUseCase
import ir.jatlin.topmarket.core.network.model.product.NetworkProductDetails
import ir.jatlin.topmarket.ui.util.stateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    private val fetchProductDetails: FetchProductDetailsUseCase,
    state: SavedStateHandle
) : ViewModel() {
    private val productId = state.getLiveData<Int>("productId").asFlow()

    private val _productDetails = MutableStateFlow(emptyProduct)
    val productDetails = _productDetails.asStateFlow()

    private val _addToCartCount = MutableStateFlow(0)
    val addToCartCount = _addToCartCount.asStateFlow()

    val productDetailsState = stateFlow {
        productId.map { fetchProductDetails(it) }
    }


    fun updateUiStatesWith(productDetails: NetworkProductDetails) {
        Timber.d("\n\n\n\n$_productDetails\n\n\n\n")
        _productDetails.value = productDetails
    }

    fun addToCart() {
        _addToCartCount.value++
    }


    fun toggleFavorite() {

    }
}

val emptyProduct = NetworkProductDetails(
    emptyList(),
    "",
    false,
    "",
    false,
    emptyList(),
    "",
    "",
    emptyList(),
    "",
    0,
    emptyList(),
    0,
    "test product",
    false,
    1,
    "",
    "2.5",
    false,
    4,
    "4",
    emptyList(),
    "",
    emptyList(),
    3,
    1,
    emptyList(),
    ""
)


