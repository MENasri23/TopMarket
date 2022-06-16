package ir.jatlin.topmarket.core.data.source.local.customer

import ir.jatlin.topmarket.core.database.entity.CustomerEntity
import ir.jatlin.topmarket.core.database.entity.CustomerWithOrders
import kotlinx.coroutines.flow.Flow

interface CustomerLocalDataSource {

    fun getCustomerWithOrders(customerId: Int): Flow<List<CustomerWithOrders>>

    suspend fun findCustomerById(id: Int): CustomerEntity?

    fun findCustomerByEmail(email: String): CustomerEntity?

    suspend fun save(customer: CustomerEntity): Int

    fun findCustomerByIdStream(customerId: Int): Flow<CustomerEntity?>

}