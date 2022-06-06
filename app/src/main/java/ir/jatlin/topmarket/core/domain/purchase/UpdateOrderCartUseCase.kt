package ir.jatlin.topmarket.core.domain.purchase

import ir.jatlin.topmarket.core.data.source.local.datastore.PurchasePreferences
import ir.jatlin.topmarket.core.data.source.local.datastore.PurchasePrefsInfo
import ir.jatlin.topmarket.core.data.di.IODispatcher
import ir.jatlin.topmarket.core.data.repository.order.OrderRepository
import ir.jatlin.topmarket.core.domain.FlowUseCase
import ir.jatlin.topmarket.core.model.order.Order
import ir.jatlin.topmarket.core.model.order.OrderLineItem
import ir.jatlin.topmarket.core.model.user.Customer
import ir.jatlin.topmarket.core.shared.fail.ErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class UpdateOrderCartUseCase @Inject constructor(
    private val orderRepository: OrderRepository,
    private val purchasePreferences: PurchasePreferences,
    errorHandler: ErrorHandler,
    @IODispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<OrderLineItem, Unit>(errorHandler, dispatcher) {

    override fun execute(params: OrderLineItem): Flow<Unit> {
        return purchasePreferences.purchasePreferencesStream.map { purchasePreferencesInfo ->
            val (customerId, activeOrderId) = purchasePreferencesInfo

            val noActiveOrder = activeOrderId == PurchasePrefsInfo.NO_ACTIVE_ORDER
            val isGuestCustomer = customerId == PurchasePrefsInfo.GUEST_CUSTOMER
            when {
                noActiveOrder && isGuestCustomer -> {
                    val orderId = orderRepository.createOrder(Order.Empty)

                    purchasePreferences.saveAvtiveOrderId(orderId)
                }
                noActiveOrder -> {
                    val order = Order.Empty.copy(
                        orderItems = listOf(params),
                        customer = Customer.Empty.copy(id = customerId)
                    )
                    val orderId = orderRepository.createOrder(order)
                    purchasePreferences.saveAvtiveOrderId(orderId)
                }
                else -> {
                    val order = orderRepository.findOrderById(activeOrderId)

                    val newOrderItems = order.orderItems.toMutableList()
                    val inList = newOrderItems.withIndex()
                        .find { it.value.id == params.id }

                    if (inList != null) {
                        newOrderItems[inList.index] = params
                    } else {
                        newOrderItems.add(params)
                    }

                    Timber.d("updateUseCase-orderItems: $newOrderItems")

                    orderRepository.updateOrder(
                        order.copy(orderItems = newOrderItems)
                    )

                }

            }

        }
    }
}