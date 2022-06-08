package ir.jatlin.topmarket.core.database.dao

import androidx.room.*
import ir.jatlin.topmarket.core.database.entity.CustomerEntity
import ir.jatlin.topmarket.core.database.entity.CustomerWithOrders
import ir.jatlin.topmarket.core.model.user.Customer
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

    @Query("SELECT * FROM customers WHERE id = :id")
    suspend fun findCustomerById(id: Int): CustomerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(customer: CustomerEntity): Long

}