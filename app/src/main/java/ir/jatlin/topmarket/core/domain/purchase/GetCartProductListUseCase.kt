package ir.jatlin.topmarket.core.domain.purchase

import ir.jatlin.topmarket.core.data.di.IODispatcher
import ir.jatlin.topmarket.core.data.repository.ProductRepository
import ir.jatlin.topmarket.core.domain.CoroutineUseCase
import ir.jatlin.topmarket.core.model.order.OrderLineItem
import ir.jatlin.topmarket.core.model.product.CartProduct
import ir.jatlin.topmarket.core.shared.fail.ErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetCartProductListUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    errorHandler: ErrorHandler,
    @IODispatcher dispatcher: CoroutineDispatcher
) : CoroutineUseCase<List<OrderLineItem>, List<CartProduct>>(errorHandler, dispatcher) {

    override suspend fun execute(params: List<OrderLineItem>): List<CartProduct> {
        return params.map { orderLineItem ->
            val product = productRepository.findProductDetailsById(orderLineItem.productId)
            CartProduct(
                productId = product.id,
                productName = product.name,
                weight = product.weight,
                orderLineId = orderLineItem.id,
                quantity = orderLineItem.quantity,
                totalPrice = orderLineItem.totalPrice,
                regularPrice = product.regularPrice,
                url = product.images.firstOrNull()?.url
            )
        }
    }
}