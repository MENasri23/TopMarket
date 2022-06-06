package ir.jatlin.topmarket.core.data.repository.orderlineitem

import ir.jatlin.topmarket.core.database.entity.OrderLineItemEntity
import ir.jatlin.topmarket.core.model.order.OrderLineItem
import kotlinx.coroutines.flow.Flow

interface OrderLineItemRepository {

    suspend fun save(orderLineItems: List<OrderLineItemEntity>)

    suspend fun update(orderLineItems: List<OrderLineItemEntity>)

    suspend fun cleatAll()

    fun findOrderLineItemByProductId(productId: Int): Flow<OrderLineItem?>
}