package ir.jatlin.topmarket.core.data.repository.customer

import ir.jatlin.topmarket.core.model.user.Customer
import kotlinx.coroutines.flow.Flow

interface CustomerRepository {

    suspend fun findCustomerById(
        customerId: Int
    ): Customer?

    suspend fun findCustomerByEmail(email: String): Customer?

    suspend fun createCustomer(
        customer: Customer
    ): Customer

    suspend fun createCustomerByEmail(email: String): Int

    suspend fun updateCustomer(
        customer: Customer
    ): Customer

    fun findCustomerByIdStream(customerId: Int): Flow<Customer?>
}