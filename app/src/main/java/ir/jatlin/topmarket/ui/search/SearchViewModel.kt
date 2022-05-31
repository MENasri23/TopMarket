package ir.jatlin.topmarket.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jatlin.topmarket.core.domain.product.FetchProductsListUseCase
import ir.jatlin.topmarket.core.domain.search.SearchProductsUseCase
import ir.jatlin.topmarket.core.domain.util.makeProductParams
import ir.jatlin.topmarket.core.network.model.product.NetworkProduct
import ir.jatlin.topmarket.core.network.model.product.category.NetworkCategory
import ir.jatlin.topmarket.core.shared.Resource
import ir.jatlin.topmarket.core.shared.fail.ErrorCause
import ir.jatlin.topmarket.core.shared.isSuccess
import ir.jatlin.topmarket.ui.search.filter.SearchProductInCategoryItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchProductsUseCase: SearchProductsUseCase,
    private val fetchProductsListUseCase: FetchProductsListUseCase
) : ViewModel() {

    init {
        Timber.d("SearchViewModel is creating")
    }

    private val _searchResult = MutableStateFlow<SearchMatchedResult?>(null)
    val searchResult = _searchResult.asStateFlow()

    private val _error = MutableSharedFlow<ErrorCause?>()
    val error = _error.asSharedFlow()

    private val _productsInCategory = MutableStateFlow<List<SearchProductInCategoryItem>?>(null)
    val productsInCategory = _productsInCategory.asStateFlow()

    private var prevSearchResult: SearchMatchedResult? = null

    private var textQuery: String = ""

    private var searchJob: Job? = null


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
            _searchResult.value = convertToSearchMatchedResult(result.data!!)
            prevSearchResult = this.searchResult.value
        } else {
            if (result is Resource.Error) {
                _error.emit(result.cause)
            }
            clearSearch()
        }
    }

    private fun convertToSearchMatchedResult(products: List<NetworkProduct>): SearchMatchedResult? {
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

        return SearchMatchedResult(header = headerItem, body = bodyItems)
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

    fun searchProductsWith(categoryId: Int) {
        val query = textQuery
        Timber.d("searchViewModel query: $query")
        val params = makeProductParams {
            if (categoryId != INVALID_ID) {
                this.categoryId = categoryId
            }
            if (query.isNotBlank()) {
                searchQuery = query
            }
        }

        viewModelScope.launch {
            fetchProductsListUseCase(params)
                .collect {
                    processProductsInCategoryResult(it)
                }
        }

    }

    private suspend fun processProductsInCategoryResult(result: Resource<List<NetworkProduct>>) {
        result.processTo { products ->
            _productsInCategory.emit(
                products.map(::SearchProductInCategoryItem)
            )
        }
    }

    private suspend fun <T> Resource<T>.processTo(
        onSuccess: suspend (result: T) -> Unit
    ) {
        if (isSuccess) {
            onSuccess(data!!)
        } else {
            if (this is Resource.Error) {
                _error.emit(cause)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Timber.d("SearchViewModel cleared...")
    }

    companion object {
        private const val INVALID_ID = -1
    }

}

data class SearchMatchedResult(
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
