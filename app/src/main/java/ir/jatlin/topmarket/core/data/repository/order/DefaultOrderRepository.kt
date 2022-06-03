package ir.jatlin.topmarket.core.data.repository.order

import ir.jatlin.topmarket.core.data.mapper.asOrderEntity
import ir.jatlin.topmarket.core.data.source.local.OrderDatabaseDataSource
import ir.jatlin.topmarket.core.data.source.remote.order.OrderRemoteDataSource
import ir.jatlin.topmarket.core.database.entity.asOrder
import ir.jatlin.topmarket.core.model.order.Order
import javax.inject.Inject

class DefaultOrderRepository @Inject constructor(
    private val remoteDataSource: OrderRemoteDataSource,
    private val localDataSource: OrderDatabaseDataSource
) : OrderRepository {

    override suspend fun findOrderById(orderId: Int): Order {
        TODO()
    }

    override suspend fun createOrder(): Order? {
        val orderNetwork = remoteDataSource.createEmptyOrder()
        localDataSource.createOrder(orderNetwork.asOrderEntity())

        return localDataSource.findOrderById(orderNetwork.id)?.asOrder()
    }

    override suspend fun updateOrder(order: Order): Order {
        TODO()
    }
}