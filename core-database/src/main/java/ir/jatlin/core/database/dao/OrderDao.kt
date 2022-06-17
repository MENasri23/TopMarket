package ir.jatlin.core.database.dao

import androidx.room.*
import ir.jatlin.core.database.entity.OrderEntity
import ir.jatlin.core.database.entity.OrderWithOrderItems
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    @Query(
        value = """
        SELECT * FROM orders WHERE id = :id
    """
    )
    @Transaction
    fun getOrderById(id: Int): OrderWithOrderItems?


    @Query(
        value = """
        SELECT * FROM orders WHERE id = :id
    """
    )
    @Transaction
    fun getOrderByIdStream(id: Int): Flow<OrderWithOrderItems?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(order: OrderEntity): Long
}