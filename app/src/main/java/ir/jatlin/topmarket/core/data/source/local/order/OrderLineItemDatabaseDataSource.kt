package ir.jatlin.topmarket.core.data.source.local.order

import ir.jatlin.core.database.dao.OrderItemDao
import ir.jatlin.core.database.entity.OrderLineItemEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrderLineItemDatabaseDataSource @Inject constructor(
    private val orderItemDao: OrderItemDao
) {

    suspend fun save(orderLineItems: List<OrderLineItemEntity>) {
        orderItemDao.insert(orderLineItems)
    }

    suspend fun update(orderLineItems: List<OrderLineItemEntity>) {
        orderItemDao.updateAndRemoveOthers(orderLineItems)
    }

    suspend fun cleatAll() {
        orderItemDao.clearAll()
    }

    fun findOrderLineItem(orderId: Int, productId: Int): Flow<OrderLineItemEntity?> {
        return orderItemDao.findOrderLineItem(orderId, productId)
    }
}