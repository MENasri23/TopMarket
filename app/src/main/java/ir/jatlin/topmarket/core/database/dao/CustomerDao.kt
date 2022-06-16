package ir.jatlin.topmarket.core.database.dao

import androidx.room.*
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

    @Query("SELECT * FROM customers WHERE id = :id")
    suspend fun findCustomerById(id: Int): CustomerEntity?

    @Query("SELECT * FROM customers WHERE id = :customerId")
    fun findCustomerByIdStream(customerId: Int): Flow<CustomerEntity?>

    @Query("SELECT * FROM customers WHERE email = :email")
    fun findCustomerByEmail(email: String): CustomerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(customer: CustomerEntity): Long

}