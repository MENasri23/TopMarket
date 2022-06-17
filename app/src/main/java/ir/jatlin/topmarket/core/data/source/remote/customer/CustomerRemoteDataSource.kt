package ir.jatlin.topmarket.core.data.source.remote.customer

import ir.jatlin.core.network.model.costumer.CustomerNetwork

interface CustomerRemoteDataSource {

    suspend fun findCustomerById(
        customerId: Int
    ): CustomerNetwork

    suspend fun findCustomerByEmail(email: String): List<CustomerNetwork>

    suspend fun createCustomer(
        customerNetwork: CustomerNetwork
    ): CustomerNetwork

    suspend fun updateCustomer(
        customer: CustomerNetwork
    ): CustomerNetwork
}