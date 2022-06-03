package ir.jatlin.topmarket.core.data.repository.order

import ir.jatlin.topmarket.core.model.order.Order

interface OrderRepository {

    suspend fun findOrderById(orderId: Int): Order

    suspend fun createOrder(): Order?

    suspend fun updateOrder(order: Order): Order

}