package ir.jatlin.topmarket.core.data.source.local

import ir.jatlin.topmarket.core.database.dao.OrderItemDao
import ir.jatlin.topmarket.core.database.entity.OrderLineItemEntity
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

    fun findOrderLineItemByProductId(productId: Int): Flow<OrderLineItemEntity?> {
        return orderItemDao.findOrderLineItemByProductId(productId)
    }
}