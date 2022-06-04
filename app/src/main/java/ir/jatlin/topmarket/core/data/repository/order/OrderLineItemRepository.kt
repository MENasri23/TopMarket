package ir.jatlin.topmarket.core.data.repository.order

import ir.jatlin.topmarket.core.database.dao.OrderItemDao
import ir.jatlin.topmarket.core.database.entity.OrderLineItemEntity
import javax.inject.Inject


class OrderLineItemDataSource @Inject constructor(
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
}