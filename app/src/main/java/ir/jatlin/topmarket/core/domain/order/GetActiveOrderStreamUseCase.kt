package ir.jatlin.topmarket.core.domain.order

import ir.jatlin.topmarket.core.data.di.IODispatcher
import ir.jatlin.topmarket.core.data.repository.order.OrderRepository
import ir.jatlin.topmarket.core.data.source.local.datastore.MarketPreferences
import ir.jatlin.topmarket.core.domain.FlowUseCase
import ir.jatlin.topmarket.core.model.order.Order
import ir.jatlin.topmarket.core.model.purchase.PurchasePrefsInfo
import ir.jatlin.topmarket.core.shared.fail.ErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetActiveOrderStreamUseCase @Inject constructor(
    private val orderRepository: OrderRepository,
    private val marketPreferences: MarketPreferences,
    errorHandler: ErrorHandler,
    @IODispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, Order?>(errorHandler, dispatcher) {

    override fun execute(params: Unit): Flow<Order?> {
        return flow {
            val activeOrderId = marketPreferences
                .purchasePreferencesStream
                .firstOrNull()?.activeOrderId ?: return@flow

            if (activeOrderId != PurchasePrefsInfo.NO_ACTIVE_ORDER) {
                emitAll(orderRepository.getOrderStream(activeOrderId))
            } else emit(null)
        }

    }
}