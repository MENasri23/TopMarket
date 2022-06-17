package ir.jatlin.topmarket.core.domain.product

import ir.jatlin.core.model.product.Product
import ir.jatlin.core.shared.fail.ErrorHandler
import ir.jatlin.topmarket.core.data.di.IODispatcher
import ir.jatlin.topmarket.core.data.repository.ProductRepository
import ir.jatlin.topmarket.core.domain.CoroutineUseCase
import ir.jatlin.topmarket.core.domain.param.DiscoverParameters
import ir.jatlin.topmarket.core.domain.util.makeProductParams
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class FetchProductsInDateRangeUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    errorHandler: ErrorHandler,
    @IODispatcher dispatcher: CoroutineDispatcher
) : CoroutineUseCase<Pair<String?, String?>, List<Product>>(errorHandler, dispatcher) {

    override suspend fun execute(params: Pair<String?, String?>): List<Product> {
        val (afterDate, beforeDate) = params
        val productParams = makeProductParams {
            pageSize = DiscoverParameters.PAGE_SIZE_INFINITE
            after = afterDate
            before = beforeDate
        }
        return productRepository.getProductsList(
            page = productParams.page,
            pageSize = productParams.pageSize,
            filters = productParams.filters
        )
    }
}