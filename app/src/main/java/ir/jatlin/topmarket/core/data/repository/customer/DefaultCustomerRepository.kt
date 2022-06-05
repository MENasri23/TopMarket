package ir.jatlin.topmarket.core.data.repository.customer

import ir.jatlin.topmarket.core.data.mapper.asCustomerEntity
import ir.jatlin.topmarket.core.data.mapper.asCustomerNetwork
import ir.jatlin.topmarket.core.data.source.local.CustomerLocalDataSource
import ir.jatlin.topmarket.core.data.source.remote.customer.CustomerRemoteDataSource
import ir.jatlin.topmarket.core.database.entity.asCustomer
import ir.jatlin.topmarket.core.model.user.Customer
import ir.jatlin.topmarket.core.network.model.costumer.CustomerNetwork
import javax.inject.Inject

class DefaultCustomerRepository @Inject constructor(
    private val remoteDataSource: CustomerRemoteDataSource,
    private val localDataSource: CustomerLocalDataSource
) : CustomerRepository {

    override suspend fun findCustomerById(customerId: Int): Customer {
        val local = localDataSource.findCustomerById(customerId)
        if (local != null) {
            return local.asCustomer()
        }
        val customerNetwork = remoteDataSource.findCustomerById(customerId)
        return saveOrUpdateLocal(customerNetwork)
    }

    override suspend fun createCustomer(customer: Customer): Customer {
        val customerNetwork = remoteDataSource.createCustomer(
            customer.asCustomerNetwork()
        )
        return saveOrUpdateLocal(customerNetwork)
    }

    override suspend fun updateCustomer(customer: Customer): Customer {
        val customerNetwork = remoteDataSource.updateCustomer(
            customer.asCustomerNetwork()
        )
        return saveOrUpdateLocal(customerNetwork)
    }

    private suspend fun saveOrUpdateLocal(customerNetwork: CustomerNetwork): Customer {
        val customerEntity = customerNetwork.asCustomerEntity()
        localDataSource.save(customerEntity)
        return customerEntity.asCustomer()
    }


}