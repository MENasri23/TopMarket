package ir.jatlin.topmarket.core.data.repository.customer

import ir.jatlin.topmarket.core.data.source.remote.customer.CustomerRemoteDataSource
import ir.jatlin.topmarket.core.model.user.Customer
import ir.jatlin.topmarket.core.model.user.asCustomer
import ir.jatlin.topmarket.core.network.model.costumer.CustomerNetwork
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultCustomerRepository @Inject constructor(
    private val remoteDataSource: CustomerRemoteDataSource,
) : CustomerRepository {

    override fun findCustomerById(customerId: Int): Flow<Customer> {
        return flow {
            emit(remoteDataSource.findCustomerById(customerId).asCustomer())
        }
    }

    override suspend fun createCustomer(customerNetwork: CustomerNetwork): Customer {
        return remoteDataSource.createCustomer(customerNetwork).asCustomer()
    }

    override suspend fun updateCustomer(customer: CustomerNetwork): Customer {
        return remoteDataSource.updateCustomer(customer).asCustomer()
    }
}