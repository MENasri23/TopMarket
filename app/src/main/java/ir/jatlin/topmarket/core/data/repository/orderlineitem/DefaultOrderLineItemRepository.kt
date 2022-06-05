package ir.jatlin.topmarket.core.data.repository.orderlineitem

import ir.jatlin.topmarket.core.data.source.local.OrderLineItemDatabaseDataSource
import ir.jatlin.topmarket.core.database.entity.OrderLineItemEntity
import ir.jatlin.topmarket.core.database.entity.asOrderLineItem
import ir.jatlin.topmarket.core.model.order.OrderLineItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
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

    override fun findOrderLineItemByProductId(productId: Int): Flow<OrderLineItem?> {
        return localDataSource.findOrderLineItemByProductId(productId)
            .distinctUntilChanged()
            .map { it?.asOrderLineItem() }
    }

}