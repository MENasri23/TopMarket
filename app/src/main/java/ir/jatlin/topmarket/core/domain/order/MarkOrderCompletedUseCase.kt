package ir.jatlin.topmarket.core.domain.order

import ir.jatlin.topmarket.core.data.di.IODispatcher
import ir.jatlin.topmarket.core.data.repository.order.OrderRepository
import ir.jatlin.topmarket.core.data.source.local.datastore.MarketPreferences
import ir.jatlin.topmarket.core.domain.CoroutineUseCase
import ir.jatlin.topmarket.core.model.order.Order
import ir.jatlin.topmarket.core.model.order.OrderStatus
import ir.jatlin.topmarket.core.shared.fail.ErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class MarkOrderCompletedUseCase @Inject constructor(
    private val orderRepository: OrderRepository,
    private val marketPreferences: MarketPreferences,
    errorHandler: ErrorHandler,
    @IODispatcher dispatcher: CoroutineDispatcher
) : CoroutineUseCase<Int, Order>(errorHandler, dispatcher) {

    override suspend fun execute(params: Int): Order {
        val order = orderRepository.findOrderById(params)
        val completedOrder = orderRepository.updateOrder(
            order.copy(status = OrderStatus.Completed)
        )
        marketPreferences.clearActiveOrder()
        return completedOrder
    }
}