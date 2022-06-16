package ir.jatlin.topmarket.core.domain.purchase

import ir.jatlin.topmarket.core.data.di.IODispatcher
import ir.jatlin.topmarket.core.data.repository.order.OrderRepository
import ir.jatlin.topmarket.core.data.source.local.datastore.MarketPreferences
import ir.jatlin.topmarket.core.domain.CoroutineUseCase
import ir.jatlin.topmarket.core.model.order.Order
import ir.jatlin.topmarket.core.model.order.OrderLineItem
import ir.jatlin.topmarket.core.model.purchase.PurchasePrefsInfo
import ir.jatlin.topmarket.core.model.user.Customer
import ir.jatlin.topmarket.core.shared.fail.ErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import timber.log.Timber
import javax.inject.Inject

class UpdateOrderCartUseCase @Inject constructor(
    private val orderRepository: OrderRepository,
    private val marketPreferences: MarketPreferences,
    errorHandler: ErrorHandler,
    @IODispatcher dispatcher: CoroutineDispatcher
) : CoroutineUseCase<OrderLineItem, Unit>(errorHandler, dispatcher) {

    override suspend fun execute(params: OrderLineItem) {
        val purchasePreferencesInfo = marketPreferences.purchasePreferencesStream.first()
        Timber.d("$purchasePreferencesInfo")
        val (customerId, activeOrderId) = purchasePreferencesInfo

        val noActiveOrder = activeOrderId == PurchasePrefsInfo.NO_ACTIVE_ORDER
        val isGuestCustomer = customerId == PurchasePrefsInfo.GUEST_CUSTOMER
        when {
            noActiveOrder && isGuestCustomer -> {
                Timber.d("Create new empty order for guest customer")
                val orderId = orderRepository.createOrder(
                    Order.Empty.copy(
                        orderItems = listOf(params)
                    )
                )
                Timber.d("new order id: $orderId")
                marketPreferences.saveActiveOrderId(orderId)
            }
            noActiveOrder -> {
                Timber.d("Create new empty order for customer with id: $customerId")
                val order = Order.Empty.copy(
                    orderItems = listOf(params),
                    customer = Customer.Empty.copy(id = customerId)
                )
                val orderId = orderRepository.createOrder(order)
                Timber.d("new order id: $orderId")
                marketPreferences.saveActiveOrderId(orderId)
            }
            else -> {
                val order = orderRepository.findOrderById(activeOrderId)
                Timber.d("update the order: $order, with orderLineItem: $params")

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