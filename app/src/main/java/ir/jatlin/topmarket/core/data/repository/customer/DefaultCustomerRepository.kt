package ir.jatlin.topmarket.core.data.repository.customer

import ir.jatlin.core.model.user.Customer
import ir.jatlin.topmarket.core.data.mapper.asCustomerEntity
import ir.jatlin.topmarket.core.data.mapper.asCustomerNetwork
import ir.jatlin.topmarket.core.data.source.local.customer.CustomerLocalDataSource
import ir.jatlin.topmarket.core.data.source.remote.NetworkException
import ir.jatlin.topmarket.core.data.source.remote.customer.CustomerRemoteDataSource
import ir.jatlin.topmarket.core.database.entity.asCustomer
import ir.jatlin.topmarket.core.network.model.costumer.CustomerNetwork
import ir.jatlin.topmarket.core.shared.fail.ErrorCause
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import javax.inject.Inject

class DefaultCustomerRepository @Inject constructor(
    private val remoteDataSource: CustomerRemoteDataSource,
    private val localDataSource: CustomerLocalDataSource
) : CustomerRepository {

    private var currentCustomer: Customer? = null
    private val mutex = Mutex()

    override suspend fun findCustomerById(customerId: Int): Customer? {
        if (currentCustomer?.id == customerId) return currentCustomer

        val local = localDataSource.findCustomerById(customerId)
        if (local != null) {
            return local.asCustomer().also {
                mutex.withLock { currentCustomer = it }
            }
        }
        return try {
            val customerNetwork = remoteDataSource.findCustomerById(customerId)
            saveOrUpdateLocal(customerNetwork).also {
                mutex.withLock { currentCustomer = it }
            }
        } catch (e: NetworkException) {
            val error = e.error
            if (error.identity == INVALID_ID &&
                error.status.code == ErrorCause.StatusCode.NOT_FOUND
            ) {
                Timber.d("Invalid local customer id: $customerId")
                null
            } else throw e
        }
    }

    override fun findCustomerByIdStream(customerId: Int): Flow<Customer?> {
        return localDataSource.findCustomerByIdStream(customerId).map {
            it?.asCustomer()
        }
    }

    override suspend fun findCustomerByEmail(email: String): Customer? {
        val local = localDataSource.findCustomerByEmail(email)
        if (local != null) {
            return local.asCustomer()
        }

        val remoteResult = remoteDataSource.findCustomerByEmail(email)
        return if (remoteResult.isNotEmpty()) {
            saveOrUpdateLocal(remoteResult.first())
        } else null

    }

    override suspend fun createCustomer(customer: Customer): Customer {
        val customerNetwork = remoteDataSource.createCustomer(
            customer.asCustomerNetwork()
        )
        return saveOrUpdateLocal(customerNetwork)
    }

    override suspend fun createCustomerByEmail(email: String): Int {
        val customerNetwork = remoteDataSource.createCustomer(
            Customer.Empty.copy(email = email).asCustomerNetwork()
        )
        return localDataSource.save(customerNetwork.asCustomerEntity())
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

    private fun invalidateCache() {
        currentCustomer = null
    }

    companion object {
        private const val INVALID_ID = "woocommerce_rest_invalid_id"
    }

}

