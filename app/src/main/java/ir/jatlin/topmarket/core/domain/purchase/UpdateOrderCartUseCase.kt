package ir.jatlin.topmarket.core.domain.purchase

import ir.jatlin.topmarket.core.data.datastore.PurchasePreferences
import ir.jatlin.topmarket.core.data.datastore.PurchasePrefsInfo
import ir.jatlin.topmarket.core.data.di.IODispatcher
import ir.jatlin.topmarket.core.data.repository.customer.CustomerRepository
import ir.jatlin.topmarket.core.data.repository.order.OrderRepository
import ir.jatlin.topmarket.core.domain.FlowUseCase
import ir.jatlin.topmarket.core.model.order.Order
import ir.jatlin.topmarket.core.model.order.OrderLineItem
import ir.jatlin.topmarket.core.model.user.Customer
import ir.jatlin.topmarket.core.shared.fail.ErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UpdateOrderCartUseCase @Inject constructor(
    private val orderRepository: OrderRepository,
    private val purchasePreferences: PurchasePreferences,
    errorHandler: ErrorHandler,
    @IODispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<List<OrderLineItem>?, Unit>(errorHandler, dispatcher) {

    override fun execute(params: List<OrderLineItem>?): Flow<Unit> {
        return purchasePreferences.purchasePreferencesStream.map {
            val (customerId, activeOrderId) = it
            val orderLineItems = params ?: emptyList()

            val noActiveOrder = activeOrderId == PurchasePrefsInfo.NO_ACTIVE_ORDER
            val isGuestCustomer = customerId == PurchasePrefsInfo.GUEST_CUSTOMER
            when {
                noActiveOrder && isGuestCustomer -> {
                    val tempOrder = Order.Empty.copy(orderItems = orderLineItems)
                    val orderId = orderRepository.createOrder(tempOrder)

                    purchasePreferences.saveAvtiveOrderId(orderId)
                }
                noActiveOrder -> {
                    val order = Order.Empty.copy(
                        customer = Customer.Empty.copy(id = customerId)
                    )
                    val orderId = orderRepository.createOrder(order)
                    purchasePreferences.saveAvtiveOrderId(orderId)
                }
                else -> {
                    val order = orderRepository.findOrderById(activeOrderId)
                    orderRepository.updateOrder(
                        order.copy(orderItems = order.orderItems + orderLineItems)
                    )

                }

            }

        }
    }
}