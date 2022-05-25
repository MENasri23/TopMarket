package ir.jatlin.topmarket.ui.productdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import ir.jatlin.topmarket.core.domain.product.FetchProductDetailsUseCase
import ir.jatlin.topmarket.core.network.model.product.NetworkProductDetails
import ir.jatlin.topmarket.ui.util.stateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductDetailsViewModel @Inject constructor(
    private val fetchProductDetails: FetchProductDetailsUseCase,
    state: SavedStateHandle
) : ViewModel() {
    private val productId = state.getLiveData<Int>("productId").asFlow()

    private val _addToCartCount = MutableStateFlow(0)
    val addToCartCount = _addToCartCount.asStateFlow()

    val productDetails = stateFlow {
        productId.map { fetchProductDetails(it) }
    }


    fun updateUiStatesWith(productDetails: NetworkProductDetails) {
        /* ... */
    }

    fun addToCart() {
        _addToCartCount.value++
    }


    fun toggleFavorite() {

    }
}


