package ir.jatlin.topmarket.core.domain.order

import ir.jatlin.topmarket.core.data.repository.order.OrderRepository
import ir.jatlin.topmarket.core.domain.CoroutineUseCase
import ir.jatlin.topmarket.core.model.order.Order
import ir.jatlin.topmarket.core.shared.fail.ErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class CreateOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository,
    errorHandler: ErrorHandler,
    dispatcher: CoroutineDispatcher
) : CoroutineUseCase<CreateOrderUseCase.OrderParams, Order>(errorHandler, dispatcher) {

    override suspend fun execute(params: OrderParams): Order {
        // TODO: Create order with the customer id
        return orderRepository.createOrder(params.order)
    }

    data class OrderParams(
        val customerId: Int,
        val order: Order,
    )
}