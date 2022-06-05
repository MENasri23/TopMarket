package ir.jatlin.topmarket.core.data.repository.customer

import ir.jatlin.topmarket.core.model.user.Customer
import ir.jatlin.topmarket.core.network.model.costumer.CustomerNetwork
import kotlinx.coroutines.flow.Flow

interface CustomerRepository {

    suspend fun findCustomerById(
        customerId: Int
    ): Customer

    suspend fun createCustomer(
        customer: Customer
    ): Customer

    suspend fun updateCustomer(
        customer: Customer
    ): Customer
}