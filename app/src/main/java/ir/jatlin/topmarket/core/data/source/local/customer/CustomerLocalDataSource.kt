package ir.jatlin.topmarket.core.data.source.local.customer

import ir.jatlin.topmarket.core.database.entity.CustomerEntity
import ir.jatlin.topmarket.core.database.entity.CustomerWithOrders
import ir.jatlin.topmarket.core.model.user.Customer
import kotlinx.coroutines.flow.Flow

interface CustomerLocalDataSource {

    fun getCustomerWithOrders(customerId: Int): Flow<List<CustomerWithOrders>>

    suspend fun findCustomerById(id: Int): CustomerEntity?

    suspend fun save(customer: CustomerEntity): Int

}