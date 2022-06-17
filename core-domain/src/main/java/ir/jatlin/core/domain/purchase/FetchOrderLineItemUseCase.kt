package ir.jatlin.core.domain.purchase

import ir.jatlin.core.data.di.IODispatcher
import ir.jatlin.core.data.repository.orderlineitem.OrderLineItemRepository
import ir.jatlin.core.domain.FlowUseCase
import ir.jatlin.core.model.order.OrderLineItem
import ir.jatlin.core.shared.fail.ErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import timber.log.Timber
import javax.inject.Inject

class FetchOrderLineItemUseCase @Inject constructor(
    private val orderLineItemRepository: OrderLineItemRepository,
    errorHandler: ErrorHandler,
    @IODispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<FetchOrderLineItemUseCase.Params?, OrderLineItem?>(errorHandler, dispatcher) {


    override fun execute(params: Params?): Flow<OrderLineItem?> {
        return flow {
            if (params == null) {
                emit(null)
            } else {
                Timber.d("$params")
                emitAll(
                    orderLineItemRepository.findOrderLineItem(
                        params.orderId, params.productId
                    ).onEach { Timber.d("orderline from repo: $it") }
                )
            }
        }
    }

    data class Params(
        val orderId: Int,
        val productId: Int
    )

}