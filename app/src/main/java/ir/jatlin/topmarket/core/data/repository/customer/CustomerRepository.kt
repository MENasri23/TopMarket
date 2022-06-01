package ir.jatlin.topmarket.core.data.repository.customer

import ir.jatlin.topmarket.core.network.model.costumer.CustomerNetwork
import kotlinx.coroutines.flow.Flow

interface CustomerRepository {

    fun findCustomerById(
        customerId: Int
    ): Flow<CustomerNetwork>

    suspend fun createCustomer(
        customerNetwork: CustomerNetwork
    ): CustomerNetwork

    suspend fun updateCustomer(
        customer: CustomerNetwork
    ): CustomerNetwork
}