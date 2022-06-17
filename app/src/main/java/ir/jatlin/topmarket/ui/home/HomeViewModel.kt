package ir.jatlin.topmarket.ui.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jatlin.core.model.product.Product
import ir.jatlin.core.shared.Resource
import ir.jatlin.core.shared.fail.ErrorCause
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.core.domain.product.FetchProductBanners
import ir.jatlin.topmarket.core.domain.product.FetchProductsListStreamUseCase
import ir.jatlin.topmarket.core.domain.product.ProductDiscoverParameters
import ir.jatlin.topmarket.core.domain.product.ProductDiscoverParameters.OrderBY
import ir.jatlin.topmarket.core.domain.util.makeProductParams
import ir.jatlin.topmarket.ui.home.amazingitem.AmazingDisplayItem
import ir.jatlin.topmarket.ui.home.amazingitem.asAmazingItem
import ir.jatlin.topmarket.ui.product.asProductItem
import ir.jatlin.topmarket.ui.util.allSuccess
import ir.jatlin.topmarket.ui.util.anyLoading
import ir.jatlin.topmarket.ui.util.findAnyFailed
import ir.jatlin.topmarket.ui.util.stateFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchProductList: FetchProductsListStreamUseCase,
    private val fetchProductBanners: FetchProductBanners
) : ViewModel() {

    private val _currentBannerPosition = MutableStateFlow(0)
    val sliderItemPosition = _currentBannerPosition.asStateFlow()

    private val _error = MutableStateFlow<ErrorCause?>(null)
    val error = _error.asStateFlow()

    private val banners = stateFlow {
        fetchProductBanners()
    }

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
        fetchProducts { categoryId = 119 }
    }

    private val productsGroupItems = combine(
        latestProducts,
        popularProducts,
        topRatedProducts
    ) { latest, popular, topRated ->
        Timber.d("latest: ${latest.javaClass.simpleName}, popular: ${popular.javaClass.simpleName}, topRated: ${topRated.javaClass.simpleName}")

        when {
            anyLoading(latest, popular, topRated) -> Resource.loading()
            allSuccess(latest, popular, topRated) -> Resource.success(
                data = listOf(
                    HomeDisplayItem.ProductDisplayGroupItem(
                        label = R.string.products_latest,
                        products = latest.data!!.map(Product::asProductItem)
                    ),
                    HomeDisplayItem.ProductDisplayGroupItem(
                        label = R.string.products_popular,
                        products = popular.data!!.map(Product::asProductItem)
                    ),
                    HomeDisplayItem.ProductDisplayGroupItem(
                        products = topRated.data!!.map(Product::asProductItem),
                        label = R.string.products_top_rated
                    )
                )
            )
            else -> findAnyFailed(latest, popular, topRated).let { error ->
                Resource.error(error?.cause ?: ErrorCause.Unknown())
            }
        }

    }

    val homeUiState: Flow<Resource<HomeUiState>> = combine(
        banners,
        amazingSuggestions,
        productsGroupItems,
    ) { banners, amazing, productGroups ->
        Timber.d("banners: ${banners.javaClass.simpleName}, amazing: ${amazing.javaClass.simpleName}")
        when {
            anyLoading(banners, amazing, productGroups) -> Resource.loading()
            allSuccess(banners, amazing, productGroups) -> {
                val result = mutableListOf<HomeDisplayItem>(
                    HomeDisplayItem.SpecialProductsSliderItem(banners.data!!)
                )
                val amazingItems = amazing.data!!.map(Product::asAmazingItem)
                val amazingHeader = AmazingDisplayItem.Header(
                    shapeIcon = R.drawable.amazing_suggestion
                )
                val amazingGroupItems = HomeDisplayItem.AmazingSuggestionGroupItem(
                    suggestionItems = mutableListOf<AmazingDisplayItem>(
                        amazingHeader
                    ).apply {
                        addAll(amazingItems)
                    },
                    backgroundColor = 0xEF3F55
                )
                result.add(amazingGroupItems)

                productGroups.data!!.forEachIndexed { index, item ->
                    if (index == 1) {
                        result.add(
                            HomeDisplayItem.AmazingSuggestionGroupItem(
                                suggestionItems = mutableListOf<AmazingDisplayItem>(
                                    amazingHeader.copy(shapeIcon = R.drawable.amazing_suggestion2)
                                ).apply { addAll(amazingItems) },
                                backgroundColor = 0x41b10b
                            )
                        )
                    }
                    result.add(item)
                }

                Resource.success(HomeUiState(result))
            }
            else -> findAnyFailed(banners, amazing, productGroups).let { error ->
                Resource.error(error?.cause ?: ErrorCause.Unknown())
            }
        }

    }


    private inline fun fetchProducts(
        crossinline block: ProductDiscoverParameters.() -> Unit = {}
    ): Flow<Resource<List<Product>>> {
        return fetchProductList(
            params = makeProductParams(block)
        )
    }

    fun onSliderItemPositionChanged(currentPosition: Int) {
        _currentBannerPosition.value = currentPosition
    }

    fun showErrorMessage(error: ErrorCause?) {
        _error.value = error
    }

    fun onShowErrorCompleted() {
        _error.value = null
    }

}

data class HomeUiState(
    val homeDisplayItems: List<HomeDisplayItem>,
)