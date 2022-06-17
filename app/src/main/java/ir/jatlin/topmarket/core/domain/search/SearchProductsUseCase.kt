package ir.jatlin.topmarket.core.domain.search

import ir.jatlin.core.model.product.Product
import ir.jatlin.topmarket.core.data.di.IODispatcher
import ir.jatlin.topmarket.core.domain.product.FetchProductsListStreamUseCase
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
    private val etchProductsListStreamUseCase: FetchProductsListStreamUseCase,
    private val charSequenceDistance: CharSequenceDistance,
    @IODispatcher private val dispatcher: CoroutineDispatcher
) {

    operator fun invoke(textQuery: String, taken: Int): Flow<Resource<List<Product>>> {
        val params = makeProductParams {
            searchQuery = textQuery
        }
        return etchProductsListStreamUseCase(params = params).map { searchResult ->
            when (searchResult) {
                is Resource.Loading -> Resource.loading()
                is Resource.Error -> Resource.error(searchResult.cause ?: ErrorCause.Unknown())
                is Resource.Success -> {
                    val products = searchResult.data!!
                    try {
                        val result = products
                            .sortedBy { product ->
                                charSequenceDistance.unlimitedCompare(textQuery, product.name)
                            }
                            .take(taken)
                        Resource.success(result)
                    } catch (e: Exception) {
                        Resource.error(ErrorCause.Unknown(e))
                    }
                }
            }

        }.catch { cause -> ErrorCause.Unknown(cause) }.flowOn(dispatcher)

    }

}

