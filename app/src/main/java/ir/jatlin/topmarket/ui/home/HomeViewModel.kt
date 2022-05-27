package ir.jatlin.topmarket.ui.home

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.core.domain.product.FetchProductsListUseCase
import ir.jatlin.topmarket.core.domain.product.ProductDiscoverParameters
import ir.jatlin.topmarket.core.domain.product.ProductDiscoverParameters.OrderBY
import ir.jatlin.topmarket.core.domain.util.makeProductParams
import ir.jatlin.topmarket.core.network.model.product.NetworkProduct
import ir.jatlin.topmarket.core.shared.Resource
import ir.jatlin.topmarket.core.shared.fail.ErrorCause
import ir.jatlin.topmarket.ui.home.amazingitem.AmazingDisplayItem
import ir.jatlin.topmarket.ui.home.amazingitem.asAmazingItem
import ir.jatlin.topmarket.ui.product.asProductItem
import ir.jatlin.topmarket.ui.util.allSuccess
import ir.jatlin.topmarket.ui.util.anyLoading
import ir.jatlin.topmarket.ui.util.findAnyFailed
import ir.jatlin.topmarket.ui.util.stateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchProductList: FetchProductsListUseCase
) : ViewModel() {

    /* private val _error = MutableStateFlow<ErrorCause?>(null)
     val error = _error.asStateFlow()*/

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

    private val amazingSuggestions = stateFlow {
        fetchProducts { searchQuery = "تخفیفات" }
    }

    val homeUiState: Flow<Resource<HomeUiState>> = combine(
        latestProducts,
        popularProducts,
        topRatedProducts,
        amazingSuggestions
    ) { latest, popular, topRated, amazing ->
        Timber.d("latest: ${latest.javaClass.simpleName}, popular: ${popular.javaClass.simpleName}, topRated: ${topRated.javaClass.simpleName}, amazing: ${amazing.javaClass.simpleName}")
        when {
            anyLoading(latest, popular, topRated, amazing) -> Resource.loading()
            allSuccess(latest, popular, topRated, amazing) -> Resource.success(
                data = HomeUiState(
                    listOf(
                        HomeDisplayItem.ProductDisplayGroupItem(
                            label = R.string.products_latest,
                            products = latest.data!!.map(NetworkProduct::asProductItem)
                        ),
                        HomeDisplayItem.AmazingSuggestionGroupItem(
                            suggestionItems = mutableListOf<AmazingDisplayItem>(
                                AmazingDisplayItem.Header(
                                    shapeIcon = R.drawable.amazing_suggestion
                                )
                            ).apply {
                                addAll(amazing.data!!.map(NetworkProduct::asAmazingItem))
                            }

                        ),
                        HomeDisplayItem.ProductDisplayGroupItem(
                            label = R.string.products_popular,
                            products = popular.data!!.map(NetworkProduct::asProductItem)
                        ),
                        HomeDisplayItem.AmazingSuggestionGroupItem(
                            suggestionItems = mutableListOf<AmazingDisplayItem>(
                                AmazingDisplayItem.Header(
                                    shapeIcon = R.drawable.amazing_suggestion2
                                )
                            ).apply {
                                addAll(amazing.data!!.map(NetworkProduct::asAmazingItem))
                            },
                            backgroundColor = 0x41b10b

                        ),
                        HomeDisplayItem.ProductDisplayGroupItem(
                            products = topRated.data!!.map(NetworkProduct::asProductItem),
                            label = R.string.products_top_rated
                        )
                    )
                )
            )
            else -> findAnyFailed(latest, popular, topRated).let { error ->
                Resource.error(error?.cause ?: ErrorCause.Unknown())
            }
        }
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
    val homeDisplayItems: List<HomeDisplayItem>,
)