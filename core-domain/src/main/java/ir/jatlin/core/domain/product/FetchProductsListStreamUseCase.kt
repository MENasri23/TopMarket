package ir.jatlin.core.domain.product

import ir.jatlin.core.data.di.IODispatcher
import ir.jatlin.core.data.repository.ProductRepository
import ir.jatlin.core.domain.FlowUseCase
import ir.jatlin.core.model.product.Product
import ir.jatlin.core.shared.fail.ErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchProductsListStreamUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    errorHandler: ErrorHandler,
    @IODispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<ProductsParameters, List<Product>>(
    errorHandler,
    dispatcher
) {

    override fun execute(params: ProductsParameters): Flow<List<Product>> {
        return productRepository.getProductsListStream(
            page = params.page,
            pageSize = params.pageSize,
            filters = params.filters
        )
    }
}

