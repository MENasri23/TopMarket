package ir.jatlin.topmarket.core.domain.product

import ir.jatlin.topmarket.core.data.di.IODispatcher
import ir.jatlin.topmarket.core.data.repository.ProductRepository
import ir.jatlin.topmarket.core.domain.FlowUseCase
import ir.jatlin.topmarket.core.network.model.product.NetworkProductDetails
import ir.jatlin.topmarket.core.shared.fail.ErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchProductDetailsUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    errorHandler: ErrorHandler,
    @IODispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Int, NetworkProductDetails>(errorHandler, dispatcher) {

    override fun execute(params: Int): Flow<NetworkProductDetails> {
        return productRepository.findProductDetailsById(params)
    }
}