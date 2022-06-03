package ir.jatlin.topmarket.core.data.source.remote.order

import ir.jatlin.topmarket.core.network.model.order.OrderNetwork

interface OrderRemoteDataSource {

    suspend fun findOrderById(orderId: Int): OrderNetwork

    suspend fun createOrder(orderNetwork: OrderNetwork): OrderNetwork

    suspend fun createEmptyOrder(): OrderNetwork

    suspend fun updateOrder(order: OrderNetwork): OrderNetwork
}