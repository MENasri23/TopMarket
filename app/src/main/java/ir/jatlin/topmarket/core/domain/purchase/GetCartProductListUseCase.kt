package ir.jatlin.topmarket.core.domain.purchase

import ir.jatlin.core.data.di.IODispatcher
import ir.jatlin.core.data.repository.ProductRepository
import ir.jatlin.core.model.order.OrderLineItem
import ir.jatlin.core.model.product.CartProduct
import ir.jatlin.core.shared.fail.ErrorHandler
import ir.jatlin.topmarket.core.domain.CoroutineUseCase
import kotlinx.coroutines.CoroutineDispatcher
import timber.log.Timber
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
                totalPrice = parsePrice(orderLineItem.totalPrice),
                regularPrice = parsePrice(product.regularPrice),
                url = product.images.firstOrNull()?.url
            )
        }
    }

    fun parsePrice(price: String): Int = try {
        price.toInt().coerceAtLeast(0)
    } catch (e: NumberFormatException) {
        Timber.d("Invalid fromat for price number: $price")
        0
    }
}