package ir.jatlin.core.data.repository.order

import ir.jatlin.core.model.order.Order
import kotlinx.coroutines.flow.Flow

interface OrderRepository {

    suspend fun findOrderById(orderId: Int): Order

    suspend fun createOrder(): Int

    suspend fun createOrder(order: Order): Int

    suspend fun updateOrder(order: Order): Order

    fun getOrderStream(orderId: Int): Flow<Order?>

}