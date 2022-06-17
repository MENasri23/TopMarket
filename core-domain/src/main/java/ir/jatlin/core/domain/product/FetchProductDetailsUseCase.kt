package ir.jatlin.core.domain.product

import ir.jatlin.core.data.di.IODispatcher
import ir.jatlin.core.data.repository.ProductRepository
import ir.jatlin.core.domain.CoroutineUseCase
import ir.jatlin.core.model.product.ProductDetails
import ir.jatlin.core.shared.fail.ErrorHandler
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