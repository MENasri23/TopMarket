package ir.jatlin.topmarket.core.domain.product

import ir.jatlin.topmarket.core.data.di.IODispatcher
import ir.jatlin.topmarket.core.data.repository.ProductRepository
import ir.jatlin.topmarket.core.domain.CoroutineUseCase
import ir.jatlin.topmarket.core.model.product.Product
import ir.jatlin.topmarket.core.shared.fail.ErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class FetchProductsListUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    errorHandler: ErrorHandler,
    @IODispatcher dispatcher: CoroutineDispatcher
) : CoroutineUseCase<ProductsParameters, List<Product>>(
    errorHandler,
    dispatcher
) {

    override suspend fun execute(params: ProductsParameters): List<Product> {
        return productRepository.getProductsList(
            page = params.page,
            pageSize = params.pageSize,
            filters = params.filters
        )
    }

}

