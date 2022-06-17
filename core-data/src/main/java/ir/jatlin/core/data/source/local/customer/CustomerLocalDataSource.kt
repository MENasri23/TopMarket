package ir.jatlin.core.data.source.local.customer

import ir.jatlin.core.database.entity.CustomerEntity
import ir.jatlin.core.database.entity.CustomerWithOrders
import kotlinx.coroutines.flow.Flow

interface CustomerLocalDataSource {

    fun getCustomerWithOrders(customerId: Int): Flow<List<CustomerWithOrders>>

    suspend fun findCustomerById(id: Int): CustomerEntity?

    fun findCustomerByEmail(email: String): CustomerEntity?

    suspend fun save(customer: CustomerEntity): Int

    fun findCustomerByIdStream(customerId: Int): Flow<CustomerEntity?>

}