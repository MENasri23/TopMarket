package ir.jatlin.core.database.dao

import androidx.room.*
import ir.jatlin.core.database.entity.OrderLineItemEntity
import kotlinx.coroutines.flow.Flow

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

    @Query("SELECT * FROM order_items WHERE order_id = :orderId AND product_id = :productId")
    fun findOrderLineItem(orderId: Int, productId: Int): Flow<OrderLineItemEntity?>

    @Transaction
    suspend fun updateAndRemoveOthers(orderLineItems: List<OrderLineItemEntity>) {
        clearAll()
        insert(orderLineItems)
    }

    @Query("DELETE FROM order_items")
    suspend fun clearAll()

    @Query("SELECT * FROM order_items")
    suspend fun getAll(): List<OrderLineItemEntity>
}
