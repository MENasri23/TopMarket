package ir.jatlin.topmarket.core.data.repository.orderlineitem

import ir.jatlin.core.database.entity.OrderLineItemEntity
import ir.jatlin.core.database.entity.asOrderLineItem
import ir.jatlin.core.model.order.OrderLineItem
import ir.jatlin.topmarket.core.data.source.local.order.OrderLineItemDatabaseDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultOrderLineItemRepository @Inject constructor(
    private val localDataSource: OrderLineItemDatabaseDataSource
) : OrderLineItemRepository {

    override suspend fun save(orderLineItems: List<OrderLineItemEntity>) {
        localDataSource.save(orderLineItems)
    }

    override suspend fun update(orderLineItems: List<OrderLineItemEntity>) {
        localDataSource.update(orderLineItems)
    }

    override suspend fun cleatAll() {
        localDataSource.cleatAll()
    }

    override fun findOrderLineItem(orderId: Int, productId: Int): Flow<OrderLineItem?> {
        return localDataSource.findOrderLineItem(orderId, productId)
//            .distinctUntilChanged()
            .map { it?.asOrderLineItem() }
    }

}