package ir.jatlin.topmarket.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jatlin.core.model.category.Category
import ir.jatlin.core.model.product.Product
import ir.jatlin.topmarket.core.domain.param.DiscoverParameters
import ir.jatlin.topmarket.core.domain.product.FetchProductsListStreamUseCase
import ir.jatlin.topmarket.core.domain.product.ProductDiscoverParameters
import ir.jatlin.topmarket.core.domain.search.SearchProductsUseCase
import ir.jatlin.topmarket.core.domain.util.makeProductParams
import ir.jatlin.topmarket.core.shared.Resource
import ir.jatlin.topmarket.core.shared.fail.ErrorCause
import ir.jatlin.topmarket.ui.search.filter.SearchProductInCategoryItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

typealias OrderBy = ProductDiscoverParameters.OrderBY
typealias Order = DiscoverParameters.Order

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchProductsUseCase: SearchProductsUseCase,
    private val fetchProductsListStreamUseCase: FetchProductsListStreamUseCase
) : ViewModel() {

    init {
        Timber.d("SearchViewModel is creating")
    }

    private val _loading = MutableSharedFlow<Boolean>()
    val loading = _loading.asSharedFlow()

    private val _searchResult = MutableStateFlow<SearchMatchedResult?>(null)
    val searchResult = _searchResult.asStateFlow()

    private val _error = MutableSharedFlow<ErrorCause?>()
    val error = _error.asSharedFlow()

    private val _productsInCategory =
        MutableStateFlow<Resource<List<SearchProductInCategoryItem>>>(Resource.loading())
    val productsInCategory = _productsInCategory.asStateFlow()

    var orderBy: OrderBy = OrderBy.Date

    var order: Order = Order.Desc

    var categoryId: Int = UNKNOWN_CATEGORY

    var includeIds: String? = null

    private var prevSearchResult: SearchMatchedResult? = null

    var textQuery: String = ""
        private set

    private var searchJob: Job? = null


    private fun searchProducts(query: String) {
        searchJob = viewModelScope.launch {
            delay(700L)
            searchProductsUseCase(textQuery = query, taken = 20)
                .collect { searchResult ->
                    processSearchResult(searchResult)
                }
        }

    }

    private suspend fun processSearchResult(result: Resource<List<Product>>) {
        when (result) {
            is Resource.Error -> _loading.emit(true)
            is Resource.Loading -> _error.emit(result.cause)
            is Resource.Success -> {
                _searchResult.value = convertToSearchMatchedResult(result.data!!)
                prevSearchResult = this.searchResult.value
                return
            }
        }
        clearSearch()
    }

    private fun convertToSearchMatchedResult(products: List<Product>): SearchMatchedResult? {
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
        categoryWithProducts: Map<Category, List<Product>>
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
        largestCategory: Map.Entry<Category, List<Product>>
    ): SearchDisplayItem.HeaderItem {
        return SearchDisplayItem.HeaderItem(
            categoryId = largestCategory.key.id,
            largestCategory.value.map(Product::asSearchProductItem)
        )
    }

    private fun clearSearch() {
        _searchResult.value = null
    }

    fun onSearchTextChanged(query: String?) {
        Timber.d("onSearchTextChanged :$query")
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

    fun searchProductsWith(categoryId: Int, includeIds: String?) {
        this.categoryId = categoryId
        this.includeIds = includeIds
        searchProducts()
    }

    fun searchProducts() {
        Timber.d("searchViewModel query: $textQuery")

        viewModelScope.launch {
            val params = makeProductParams {
                if (textQuery.isNotBlank()) {
                    searchQuery = textQuery
                }
                val category = this@SearchViewModel.categoryId
                if (category != UNKNOWN_CATEGORY) {
                    categoryId = category
                }
                val ids = this@SearchViewModel.includeIds
                if (!ids.isNullOrEmpty()) {
                    try {
                        includeIds = ids.split(",".toRegex()).map(String::toInt)
                    } catch (e: NumberFormatException) {
                        Timber.d("Unable to parse $ids as list of numbers")
                    }
                }
                order = this@SearchViewModel.order
                orderBy = this@SearchViewModel.orderBy
            }
            fetchProductsListStreamUseCase(params)
                .collect {
                    processProductsInCategoryResult(it)
                }
        }
    }

    private suspend fun processProductsInCategoryResult(result: Resource<List<Product>>) {
        with(_productsInCategory) {
            when (result) {
                is Resource.Error -> emit(Resource.error(result.cause ?: ErrorCause.Unknown()))
                is Resource.Loading -> emit(Resource.loading())
                is Resource.Success -> {
                    val products = result.data!!
                    emit(
                        Resource.success(
                            products.map(::SearchProductInCategoryItem)
                        )
                    )
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Timber.d("SearchViewModel cleared...")
    }

    companion object {
        private const val UNKNOWN_CATEGORY = -1
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

fun Product.asSearchProductItem() = SearchProductItem(
    id = id,
    name = name,
    imageUrl = images.firstOrNull()?.url
)
