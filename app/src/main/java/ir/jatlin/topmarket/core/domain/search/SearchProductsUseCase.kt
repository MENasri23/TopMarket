package ir.jatlin.topmarket.core.domain.search

import ir.jatlin.topmarket.core.data.di.IODispatcher
import ir.jatlin.topmarket.core.domain.product.FetchProductsListUseCase
import ir.jatlin.topmarket.core.domain.util.CharSequenceDistance
import ir.jatlin.topmarket.core.domain.util.makeProductParams
import ir.jatlin.topmarket.core.shared.Resource
import ir.jatlin.topmarket.core.shared.fail.ErrorCause
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchProductsUseCase @Inject constructor(
    private val fetchProductsListUseCase: FetchProductsListUseCase,
    private val charSequenceDistance: CharSequenceDistance,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) {

    operator fun invoke(textQuery: String, taken: Int): Flow<Resource<List<SearchProductsResult>>> {
        val params = makeProductParams {
            searchQuery = textQuery
        }
        return fetchProductsListUseCase(params = params).map { searchResult ->
            when (searchResult) {
                is Resource.Loading -> Resource.loading()
                is Resource.Error -> Resource.error(searchResult.cause ?: ErrorCause.Unknown())
                is Resource.Success -> {
                    val products = searchResult.data!!

                    val result = products
                        .sortedBy { product ->
                            charSequenceDistance.unlimitedCompare(textQuery, product.name)
                        }
                        .take(taken)
                        .mapNotNull { product ->
                            val nestedCategory = product.categories.lastOrNull()
                                ?: return@mapNotNull null

                            SearchProductsResult(
                                productId = product.id,
                                productName = product.name,
                                categoryId = nestedCategory.id,
                                categoryName = nestedCategory.name
                            )

                        }
                    Resource.success(result)
                }
            }

        }.catch { cause -> ErrorCause.Unknown(cause) }.flowOn(dispatcher)

    }

}

data class SearchProductsResult(
    val productId: Int,
    val productName: String,
    val categoryId: Int,
    val categoryName: String
)

