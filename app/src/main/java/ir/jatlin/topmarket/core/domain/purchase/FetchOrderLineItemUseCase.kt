package ir.jatlin.topmarket.core.domain.purchase

import ir.jatlin.topmarket.core.data.di.IODispatcher
import ir.jatlin.topmarket.core.data.repository.orderlineitem.OrderLineItemRepository
import ir.jatlin.topmarket.core.domain.FlowUseCase
import ir.jatlin.topmarket.core.model.order.OrderLineItem
import ir.jatlin.topmarket.core.shared.fail.ErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchOrderLineItemUseCase @Inject constructor(
    private val orderLineItemRepository: OrderLineItemRepository,
    errorHandler: ErrorHandler,
    @IODispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Int, OrderLineItem?>(errorHandler, dispatcher) {


    override fun execute(params: Int): Flow<OrderLineItem?> {
        return orderLineItemRepository.findOrderLineItemByProductId(params)
    }
}