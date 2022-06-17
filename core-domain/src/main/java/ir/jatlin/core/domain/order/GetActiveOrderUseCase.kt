package ir.jatlin.core.domain.order

import ir.jatlin.core.data.di.IODispatcher
import ir.jatlin.core.data.repository.order.OrderRepository
import ir.jatlin.core.data.source.local.datastore.MarketPreferences
import ir.jatlin.core.domain.FlowUseCase
import ir.jatlin.core.model.order.Order
import ir.jatlin.core.model.purchase.PurchasePrefsInfo
import ir.jatlin.core.shared.fail.ErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class GetActiveOrderUseCase @Inject constructor(
    private val orderRepository: OrderRepository,
    private val marketPreferences: MarketPreferences,
    errorHandler: ErrorHandler,
    @IODispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, Order?>(errorHandler, dispatcher) {


    override fun execute(params: Unit): Flow<Order?> {
        return marketPreferences.purchasePreferencesStream.map {
            Timber.d("$it")
            val activeOrderId = it.activeOrderId
            if (activeOrderId != PurchasePrefsInfo.NO_ACTIVE_ORDER) {
                orderRepository.findOrderById(activeOrderId)
            } else null
        }
    }

}