package ir.jatlin.topmarket.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ir.jatlin.topmarket.core.database.entity.OrderEntity
import ir.jatlin.topmarket.core.database.entity.OrderWithOrderItems

@Dao
interface OrderDao {

    @Query(
        value = """
        SELECT * FROM orders WHERE id = :id
    """
    )
    fun getOrderById(id: Int): OrderWithOrderItems?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: OrderEntity): Long
}