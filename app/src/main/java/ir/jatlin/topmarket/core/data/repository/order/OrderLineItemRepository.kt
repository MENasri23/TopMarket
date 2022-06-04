package ir.jatlin.topmarket.core.data.repository.order

import ir.jatlin.topmarket.core.database.dao.OrderItemDao
import ir.jatlin.topmarket.core.database.entity.OrderLineItemEntity
import javax.inject.Inject


class OrderLineItemDataSource @Inject constructor(
    private val orderItemDao: OrderItemDao
) {

    suspend fun save(orderItems: List<OrderLineItemEntity>) {
        orderItemDao.insert(orderItems)
    }
}