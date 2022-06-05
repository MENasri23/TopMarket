package ir.jatlin.topmarket.core.database.dao

import androidx.room.*
import ir.jatlin.topmarket.core.database.entity.OrderLineItemEntity
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

    @Query("SELECT * FROM order_items WHERE product_id = :productId")
    fun findOrderLineItemByProductId(productId: Int): Flow<OrderLineItemEntity?>

    @Transaction
    suspend fun updateAndRemoveOthers(orderLineItems: List<OrderLineItemEntity>) {
        clearAll()
        insert(orderLineItems)
    }

    @Query("DELETE FROM order_items")
    suspend fun clearAll()
}
