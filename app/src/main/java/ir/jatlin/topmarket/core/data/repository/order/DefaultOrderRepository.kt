package ir.jatlin.topmarket.core.data.repository.order

import ir.jatlin.topmarket.core.data.mapper.asOrderEntity
import ir.jatlin.topmarket.core.data.mapper.asOrderLineItemEntity
import ir.jatlin.topmarket.core.data.mapper.asOrderNetwork
import ir.jatlin.topmarket.core.data.source.local.OrderDatabaseDataSource
import ir.jatlin.topmarket.core.data.source.remote.order.OrderRemoteDataSource
import ir.jatlin.topmarket.core.database.entity.asOrder
import ir.jatlin.topmarket.core.model.order.Order
import timber.log.Timber
import javax.inject.Inject

class DefaultOrderRepository @Inject constructor(
    private val remoteDataSource: OrderRemoteDataSource,
    private val localDataSource: OrderDatabaseDataSource,
    private val orderLineItemDataSource: OrderLineItemDataSource
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
    override suspend fun createOrder(): Int {
        val orderNetwork = remoteDataSource.createEmptyOrder()
        return localDataSource.saveOrder(orderNetwork.asOrderEntity()).also {
            Timber.d(
                "Created new order with localId: $it and order network id: ${orderNetwork.id}"
            )
        }

    }

    override suspend fun createOrder(order: Order): Int {
        val orderNetwork = remoteDataSource.createOrder(order.asOrderNetwork())
        val orderId = localDataSource.saveOrder(orderNetwork.asOrderEntity())
        orderLineItemDataSource.save(
            orderNetwork.lineItems.map {
                it.asOrderLineItemEntity(orderId)
            }
        )
        return orderId
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
