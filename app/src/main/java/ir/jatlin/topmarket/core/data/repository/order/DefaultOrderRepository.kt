package ir.jatlin.topmarket.core.data.repository.order

import ir.jatlin.topmarket.core.data.mapper.asOrderEntity
import ir.jatlin.topmarket.core.data.mapper.asOrderLineItemEntity
import ir.jatlin.topmarket.core.data.mapper.asOrderNetwork
import ir.jatlin.topmarket.core.data.repository.orderlineitem.OrderLineItemRepository
import ir.jatlin.topmarket.core.data.source.local.order.OrderDatabaseDataSource
import ir.jatlin.topmarket.core.data.source.remote.order.OrderRemoteDataSource
import ir.jatlin.topmarket.core.database.entity.asOrder
import ir.jatlin.topmarket.core.model.order.Order
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class DefaultOrderRepository @Inject constructor(
    private val remoteDataSource: OrderRemoteDataSource,
    private val localDataSource: OrderDatabaseDataSource,
    private val orderLineItemRepository: OrderLineItemRepository
) : OrderRepository {

    @Throws(OrderNotFoundException::class)
    override suspend fun findOrderById(orderId: Int): Order {
        val localOrder = localDataSource.findOrderById(orderId)
        if (localOrder != null) {
            return localOrder.asOrder()
        }
        val remoteOrder = remoteDataSource.findOrderById(orderId)
        localDataSource.saveOrder(remoteOrder.asOrderEntity())
        orderLineItemRepository.save(
            remoteOrder.lineItems.map {
                it.asOrderLineItemEntity(orderId)
            }
        )
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

        orderLineItemRepository.cleatAll()
        orderLineItemRepository.save(
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

        orderLineItemRepository.update(remoteOrder.lineItems.map {
            it.asOrderLineItemEntity(remoteOrder.id)
        })

        return getOrderOrThrow(id)
    }

    override fun getOrderStream(orderId: Int): Flow<Order?> {
        return localDataSource
            .findOrderByIdStream(orderId)
            .map { it?.asOrder() }
    }

    private fun getOrderOrThrow(orderId: Int) =
        localDataSource.findOrderById(orderId)?.asOrder() ?: throw OrderNotFoundException()
}

class OrderNotFoundException : Throwable()
