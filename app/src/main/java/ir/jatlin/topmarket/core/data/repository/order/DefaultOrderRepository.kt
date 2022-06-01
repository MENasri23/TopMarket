package ir.jatlin.topmarket.core.data.repository.order

import ir.jatlin.topmarket.core.data.mapper.asOrder
import ir.jatlin.topmarket.core.data.mapper.asOrderNetwork
import ir.jatlin.topmarket.core.data.source.remote.order.OrderRemoteDataSource
import ir.jatlin.topmarket.core.model.order.Order
import javax.inject.Inject

class DefaultOrderRepository @Inject constructor(
    private val remoteDataSource: OrderRemoteDataSource
) : OrderRepository {

    override suspend fun findOrderById(orderId: Int): Order {
        return remoteDataSource.findOrderById(orderId).asOrder()
    }

    override suspend fun createOrder(order: Order): Order {
        return remoteDataSource.createOrder(order.asOrderNetwork()).asOrder()
    }

    override suspend fun updateOrder(order: Order): Order {
        return remoteDataSource.updateOrder(order.asOrderNetwork()).asOrder()
    }
}