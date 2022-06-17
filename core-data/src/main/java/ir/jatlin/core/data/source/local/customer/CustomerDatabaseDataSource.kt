package ir.jatlin.core.data.source.local.customer

import ir.jatlin.core.database.dao.CustomerDao
import ir.jatlin.core.database.entity.CustomerEntity
import ir.jatlin.core.database.entity.CustomerWithOrders
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CustomerDatabaseDataSource @Inject constructor(
    private val customerDao: CustomerDao
) : CustomerLocalDataSource {

    override fun getCustomerWithOrders(customerId: Int): Flow<List<CustomerWithOrders>> {
        return customerDao.getCustomerWithOrders(customerId)
    }

    override suspend fun findCustomerById(id: Int): CustomerEntity? {
        return customerDao.findCustomerById(id)
    }

    override fun findCustomerByIdStream(customerId: Int): Flow<CustomerEntity?> {
        return customerDao.findCustomerByIdStream(customerId)
    }

    override fun findCustomerByEmail(email: String): CustomerEntity? {
        return customerDao.findCustomerByEmail(email)
    }

    override suspend fun save(customer: CustomerEntity): Int {
        return customerDao.insert(customer).toInt()
    }
}