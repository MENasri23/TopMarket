package ir.jatlin.topmarket.core.data.repository.order

import ir.jatlin.topmarket.core.data.mapper.asOrderEntity
import ir.jatlin.topmarket.core.data.mapper.asOrderNetwork
import ir.jatlin.topmarket.core.data.source.local.OrderDatabaseDataSource
import ir.jatlin.topmarket.core.data.source.remote.order.OrderRemoteDataSource
import ir.jatlin.topmarket.core.database.entity.asOrder
import ir.jatlin.topmarket.core.model.order.Order
import javax.inject.Inject

class DefaultOrderRepository @Inject constructor(
    private val remoteDataSource: OrderRemoteDataSource,
    private val localDataSource: OrderDatabaseDataSource
) : OrderRepository {

    @Throws(OrderNotFoundException::class)
    override suspend fun findOrderById(orderId: Int): Order {
        val localOrder = localDataSource.findOrderById(orderId)
        if (localOrder != null) {
            return localOrder.asOrder()
        }
        val remoteOrder = remoteDataSource.findOrderById(orderId)
        localDataSource.saveOrder(remoteOrder.asOrderEntity())

        return getOrderOrThrow(orderId)
    }

    @Throws(OrderNotFoundException::class)
    override suspend fun createOrder(): Order {
        val orderNetwork = remoteDataSource.createEmptyOrder()
        localDataSource.saveOrder(orderNetwork.asOrderEntity())
        localDataSource.findOrderById(orderNetwork.id)?.asOrder()

        return getOrderOrThrow(orderNetwork.id)
    }

    @Throws(OrderNotFoundException::class)
    override suspend fun updateOrder(order: Order): Order {
        val remoteOrder = remoteDataSource.updateOrder(order.asOrderNetwork())
        val id = localDataSource.saveOrder(remoteOrder.asOrderEntity())

        return getOrderOrThrow(id.toInt())
    }

    private fun getOrderOrThrow(orderId: Int) =
        localDataSource.findOrderById(orderId)?.asOrder() ?: throw OrderNotFoundException()
}

class OrderNotFoundException : Throwable()
