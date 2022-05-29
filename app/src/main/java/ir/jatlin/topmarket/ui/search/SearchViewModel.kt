package ir.jatlin.topmarket.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jatlin.topmarket.core.domain.param.DiscoverParameters
import ir.jatlin.topmarket.core.domain.search.SearchProductsUseCase
import ir.jatlin.topmarket.core.domain.util.makeProductParams
import ir.jatlin.topmarket.core.network.model.product.NetworkProduct
import ir.jatlin.topmarket.core.network.model.product.category.NetworkCategory
import ir.jatlin.topmarket.core.shared.Resource
import ir.jatlin.topmarket.core.shared.fail.ErrorCause
import ir.jatlin.topmarket.core.shared.isSuccess
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchProductsUseCase: SearchProductsUseCase
) : ViewModel() {

    private val _searchResult = MutableStateFlow<SearchResult?>(null)
    val searchResult = _searchResult.asStateFlow()

    private val _error = MutableSharedFlow<ErrorCause?>()
    val error = _error.asSharedFlow()

    private var prevSearchResult: SearchResult? = null

    private var textQuery: String = ""

    private var searchJob: Job? = null


    fun onSearchTextChanged(query: String?) {
        searchJob?.cancel()

        val search = query?.trim()
        if (search == null || search.length < 2) {
            clearSearch()
            return
        }

        if (textQuery != search) {
            textQuery = search

            searchProducts(search)
        } else {
            _searchResult.value = prevSearchResult
        }
    }

    private fun searchProducts(query: String) {
        searchJob = viewModelScope.launch {
            delay(500L)
            searchProductsUseCase(textQuery = query, taken = 20)
                .collect { searchResult ->
                    processSearchResult(searchResult)
                }

        }

    }

    private suspend fun processSearchResult(result: Resource<List<NetworkProduct>>) {
        if (result.isSuccess) {
            _searchResult.value = processToDisplayItems(result.data!!)
            prevSearchResult = this.searchResult.value
        } else {
            if (result is Resource.Error) {
                _error.emit(result.cause)
            }
            clearSearch()
        }
    }

    private fun processToDisplayItems(products: List<NetworkProduct>): SearchResult? {
        val categoryWithProducts = products.groupBy {
            try {
                it.categories.last()
            } catch (e: NoSuchElementException) {
                Timber.d("category not found for product with id: ${it.id}")
                return null
            }
        }

        val largestCategory = categoryWithProducts.maxByOrNull { it.value.size }

        if (largestCategory == null) {
            clearSearch()
            return null
        }

        val headerItem = getHeaderItem(largestCategory)
        val bodyItems = getBodyItems(
            largestCategoryId = largestCategory.key.id,
            categoryWithProducts = categoryWithProducts
        )

        return SearchResult(header = headerItem, body = bodyItems)
    }

    private fun getBodyItems(
        largestCategoryId: Int,
        categoryWithProducts: Map<NetworkCategory, List<NetworkProduct>>
    ): List<SearchDisplayItem.BodyItem> {
        return categoryWithProducts
            .filterNot { it.key.id == largestCategoryId }
            .map {
                val category = it.key
                val product = it.value.first()

                SearchDisplayItem.BodyItem(
                    categoryId = category.id,
                    categoryName = category.name,
                    productId = product.id,
                    productName = product.name
                )
            }
    }

    private fun getHeaderItem(
        largestCategory: Map.Entry<NetworkCategory, List<NetworkProduct>>
    ): SearchDisplayItem.HeaderItem {
        return SearchDisplayItem.HeaderItem(
            categoryId = largestCategory.key.id,
            largestCategory.value.map(NetworkProduct::asSearchProductItem)
        )
    }

    private fun clearSearch() {
        _searchResult.value = null
    }

}

data class SearchResult(
    val header: SearchDisplayItem.HeaderItem,
    val body: List<SearchDisplayItem.BodyItem>
)

data class SearchProductItem(
    val id: Int,
    val name: String,
    val imageUrl: String?
)

fun NetworkProduct.asSearchProductItem() = SearchProductItem(
    id = id,
    name = name,
    imageUrl = images.firstOrNull()?.url
)
