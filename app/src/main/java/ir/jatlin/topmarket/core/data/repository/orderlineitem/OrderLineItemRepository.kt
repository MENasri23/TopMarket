package ir.jatlin.topmarket.core.data.repository.orderlineitem

import ir.jatlin.core.database.entity.OrderLineItemEntity
import ir.jatlin.core.model.order.OrderLineItem
import kotlinx.coroutines.flow.Flow

interface OrderLineItemRepository {

    suspend fun save(orderLineItems: List<OrderLineItemEntity>)

    suspend fun update(orderLineItems: List<OrderLineItemEntity>)

    suspend fun cleatAll()

    fun findOrderLineItem(orderId: Int, productId: Int): Flow<OrderLineItem?>
}