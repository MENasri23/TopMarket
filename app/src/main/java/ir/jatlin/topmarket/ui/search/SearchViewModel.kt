package ir.jatlin.topmarket.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jatlin.topmarket.core.domain.param.DiscoverParameters
import ir.jatlin.topmarket.core.domain.product.FetchProductsListUseCase
import ir.jatlin.topmarket.core.domain.util.makeProductParams
import ir.jatlin.topmarket.core.network.model.product.NetworkProduct
import ir.jatlin.topmarket.core.shared.Resource
import ir.jatlin.topmarket.core.shared.isSuccess
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchProductsUseCase: FetchProductsListUseCase
) : ViewModel() {

    private val _searchResult = MutableStateFlow<List<NetworkProduct>>(emptyList())
    val searchResult = _searchResult.asStateFlow()

    private var prevSearchResult: List<NetworkProduct>? = null

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
            _searchResult.value = prevSearchResult ?: emptyList()
        }
    }

    private fun searchProducts(query: String) {
        searchJob = viewModelScope.launch {
            delay(500L)
            val discoverParams = makeProductParams {
                searchQuery = query
                pageSize = DiscoverParameters.PAGE_SIZE_INFINITE

            }
            searchProductsUseCase(params = discoverParams)
                .collect { searchResult ->
                    processSearchResult(searchResult)
                }

        }

    }

    private fun processSearchResult(searchResult: Resource<List<NetworkProduct>>) {
        if (searchResult.isSuccess) {
            _searchResult.value = searchResult.data!!
            prevSearchResult = this.searchResult.value
        } else {
            Timber.d("$searchResult")
        }
    }

    private fun clearSearch() {
        _searchResult.value = emptyList()
    }

}