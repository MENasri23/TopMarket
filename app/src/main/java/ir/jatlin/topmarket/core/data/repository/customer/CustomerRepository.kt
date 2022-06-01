package ir.jatlin.topmarket.core.data.repository.customer

import ir.jatlin.topmarket.core.model.user.Customer
import ir.jatlin.topmarket.core.network.model.costumer.CustomerNetwork
import kotlinx.coroutines.flow.Flow

interface CustomerRepository {

    fun findCustomerById(
        customerId: Int
    ): Flow<Customer>

    suspend fun createCustomer(
        customerNetwork: CustomerNetwork
    ): Customer

    suspend fun updateCustomer(
        customer: CustomerNetwork
    ): Customer
}