package ir.jatlin.topmarket.core.data.source.local.order

import ir.jatlin.core.database.dao.OrderDao
import ir.jatlin.core.database.entity.OrderEntity
import ir.jatlin.core.database.entity.OrderWithOrderItems
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class OrderDatabaseDataSource @Inject constructor(
    private val orderDao: OrderDao
) {

    suspend fun saveOrder(order: OrderEntity): Int {
        return orderDao.insert(order).toInt()
    }

    fun findOrderById(orderId: Int): OrderWithOrderItems? {
        return orderDao.getOrderById(id = orderId)
    }

    fun findOrderByIdStream(orderId: Int): Flow<OrderWithOrderItems?> {
        return orderDao.getOrderByIdStream(id = orderId)
    }
}