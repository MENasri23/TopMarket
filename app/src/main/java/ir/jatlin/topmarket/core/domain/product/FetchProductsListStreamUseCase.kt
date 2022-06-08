package ir.jatlin.topmarket.core.domain.product

import ir.jatlin.topmarket.core.data.di.IODispatcher
import ir.jatlin.topmarket.core.data.repository.ProductRepository
import ir.jatlin.topmarket.core.domain.FlowUseCase
import ir.jatlin.topmarket.core.network.model.product.NetworkProduct
import ir.jatlin.topmarket.core.shared.fail.ErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchProductsListStreamUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    errorHandler: ErrorHandler,
    @IODispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<ProductsParameters, List<NetworkProduct>>(
    errorHandler,
    dispatcher
) {

    override fun execute(params: ProductsParameters): Flow<List<NetworkProduct>> {
        return productRepository.getProductsListStream(
            page = params.page,
            pageSize = params.pageSize,
            filters = params.filters
        )
    }
}

