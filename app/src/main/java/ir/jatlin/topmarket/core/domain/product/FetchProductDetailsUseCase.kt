package ir.jatlin.topmarket.core.domain.product

import ir.jatlin.core.model.product.ProductDetails
import ir.jatlin.topmarket.core.data.di.IODispatcher
import ir.jatlin.topmarket.core.data.repository.ProductRepository
import ir.jatlin.topmarket.core.domain.CoroutineUseCase
import ir.jatlin.topmarket.core.shared.fail.ErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class FetchProductDetailsUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    errorHandler: ErrorHandler,
    @IODispatcher dispatcher: CoroutineDispatcher
) : CoroutineUseCase<Int, ProductDetails?>(errorHandler, dispatcher) {

    override suspend fun execute(params: Int): ProductDetails? {
        return productRepository.findProductDetailsById(params)
    }
}