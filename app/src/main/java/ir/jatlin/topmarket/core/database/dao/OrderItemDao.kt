package ir.jatlin.topmarket.core.database.dao

import androidx.room.*
import ir.jatlin.topmarket.core.database.entity.OrderLineItemEntity

@Dao
interface OrderItemDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(orderItems: List<OrderLineItemEntity>)

    @Update
    suspend fun update(orderItems: List<OrderLineItemEntity>)

    @Delete
    suspend fun delete(orderItems: List<OrderLineItemEntity>)

    @Query("DELETE FROM order_items WHERE id NOT IN (:ids)")
    suspend fun deleteAllExcept(ids: List<Int>)

    @Transaction
    suspend fun updateAndRemoveOthers(orderLineItems: List<OrderLineItemEntity>) {
        deleteAllExcept(orderLineItems.map(OrderLineItemEntity::id))
        update(orderLineItems)
    }

    @Query("DELETE FROM order_items")
    suspend fun clearAll()
}
