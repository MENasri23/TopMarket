package ir.jatlin.topmarket.core.domain.category

import ir.jatlin.topmarket.core.data.di.IODispatcher
import ir.jatlin.topmarket.core.data.repository.ProductRepository
import ir.jatlin.topmarket.core.domain.FlowUseCase
import ir.jatlin.topmarket.core.domain.product.FetchProductsListUseCase
import ir.jatlin.topmarket.core.domain.product.ProductDiscoverParameters
import ir.jatlin.topmarket.core.network.model.product.category.NetworkCategoryDetails
import ir.jatlin.topmarket.core.shared.fail.ErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchCategoryDetailsListUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    errorHandler: ErrorHandler,
    @IODispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<FetchCategoryDetailsListUseCase.Parameters, List<NetworkCategoryDetails>>(
    errorHandler,
    dispatcher
) {
    override fun execute(params: Parameters): Flow<List<NetworkCategoryDetails>> {
        return productRepository.getProductCategories(
            page = params.page,
            pageSize = params.pageSize,
            filters = params.filters
        )
    }

    data class Parameters(
        val page: Int,
        val pageSize: Int,
        val filters: Map<String, String>
    )
}