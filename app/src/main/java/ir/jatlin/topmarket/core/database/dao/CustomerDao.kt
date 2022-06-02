package ir.jatlin.topmarket.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ir.jatlin.topmarket.core.database.entity.CustomerEntity
import ir.jatlin.topmarket.core.database.entity.CustomerPurchaseProductCrossRef
import ir.jatlin.topmarket.core.database.entity.CustomerWithPurchaseProducts
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {

    @Query(
        value = """
            SELECT * FROM customers 
            WHERE id = :customerId
    """
    )
    fun getCustomerPurchaseProducts(
        customerId: Int
    ): Flow<List<CustomerWithPurchaseProducts>>

    @Insert
    suspend fun insert(customer: CustomerEntity): Int

    @Insert
    suspend fun insert(customers: List<CustomerEntity>): List<Int>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCustomerPurchaseProductCrossRef(
        customerPurchaseProductCrossReferences: List<CustomerPurchaseProductCrossRef>
    )


}