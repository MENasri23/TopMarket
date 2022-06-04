package ir.jatlin.topmarket.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import ir.jatlin.topmarket.core.database.entity.CustomerEntity
import ir.jatlin.topmarket.core.database.entity.CustomerWithOrders
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {

    @Query(
        value = """
            SELECT * FROM customers 
            WHERE id = :customerId
    """
    )
    @Transaction
    fun getCustomerWithOrders(
        customerId: Int
    ): Flow<List<CustomerWithOrders>>

    @Insert
    suspend fun insert(customer: CustomerEntity): Long

    @Insert
    suspend fun insert(customers: List<CustomerEntity>): List<Long>

}