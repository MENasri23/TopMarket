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

}