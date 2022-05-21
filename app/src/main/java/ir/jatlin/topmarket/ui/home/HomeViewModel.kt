package ir.jatlin.topmarket.ui.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jatlin.topmarket.core.domain.product.FetchProductsListUseCase
import ir.jatlin.topmarket.core.domain.product.ProductDiscoverParameters
import ir.jatlin.topmarket.core.domain.product.ProductDiscoverParameters.OrderBY
import ir.jatlin.topmarket.core.domain.product.makeProductParams
import ir.jatlin.topmarket.core.network.model.product.NetworkProduct
import ir.jatlin.topmarket.core.shared.Resource
import ir.jatlin.topmarket.core.shared.fail.ErrorCause
import ir.jatlin.topmarket.ui.util.allSuccess
import ir.jatlin.topmarket.ui.util.findAnyFailed
import ir.jatlin.topmarket.ui.util.stateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchProductList: FetchProductsListUseCase
) : ViewModel() {

    private val _error = MutableStateFlow<ErrorCause?>(null)
    val error = _error.asStateFlow()

    private val latestProducts = stateFlow {
        fetchProducts {
            orderBy = OrderBY.Date
        }
    }

    private val popularProducts = stateFlow {
        fetchProducts {
            orderBy = OrderBY.Popularity
        }
    }


    private val topRatedProducts = stateFlow {
        fetchProducts {
            orderBy = OrderBY.Rating
        }
    }

    val homeUiState: Flow<HomeUiState?> = combine(
        latestProducts,
        popularProducts,
        topRatedProducts
    ) { latest, popular, topRated ->

        if (allSuccess(latest, popular, topRated)) {
            val result = listOf(
                latest.data!!, popular.data!!, topRated.data!!
            )
            return@combine HomeUiState(
                categorizedProducts = result
            )
        }
        val error = findAnyFailed(latest, popular, topRated)
        _error.emit(error?.cause)

        null

    }


    private inline fun fetchProducts(
        crossinline block: ProductDiscoverParameters.() -> Unit = {}
    ): Flow<Resource<List<NetworkProduct>>> {
        return fetchProductList(
            params = makeProductParams(block)
        )
    }

}

data class HomeUiState(
    val categorizedProducts: List<List<NetworkProduct>>,
)
