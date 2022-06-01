package ir.jatlin.topmarket.core.data.repository.customer

import ir.jatlin.topmarket.core.data.mapper.asCustomer
import ir.jatlin.topmarket.core.data.mapper.asCustomerNetwork
import ir.jatlin.topmarket.core.data.source.remote.customer.CustomerRemoteDataSource
import ir.jatlin.topmarket.core.model.user.Customer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultCustomerRepository @Inject constructor(
    private val remoteDataSource: CustomerRemoteDataSource,
) : CustomerRepository {

    override fun findCustomerById(customerId: Int): Flow<Customer> {
        return flow {
            emit(
                remoteDataSource.findCustomerById(customerId).asCustomer()
            )
        }
    }

    override suspend fun createCustomer(customer: Customer): Customer {
        return remoteDataSource.createCustomer(
            customer.asCustomerNetwork()
        ).asCustomer()
    }

    override suspend fun updateCustomer(customer: Customer): Customer {
        return remoteDataSource.updateCustomer(
            customer.asCustomerNetwork()
        ).asCustomer()
    }
}