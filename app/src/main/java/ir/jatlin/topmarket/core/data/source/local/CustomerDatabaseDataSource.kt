package ir.jatlin.topmarket.core.data.source.local

import ir.jatlin.topmarket.core.database.dao.CustomerDao
import ir.jatlin.topmarket.core.database.entity.CustomerEntity
import ir.jatlin.topmarket.core.database.entity.CustomerWithOrders
import ir.jatlin.topmarket.core.model.user.Customer
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CustomerDatabaseDataSource @Inject constructor(
    private val customerDao: CustomerDao
) : CustomerLocalDataSource {

    override fun getCustomerWithOrders(customerId: Int): Flow<List<CustomerWithOrders>> {
        return customerDao.getCustomerWithOrders(customerId)
    }

    override suspend fun findCustomerById(id: Int): Customer? {
        return customerDao.findCustomerById(id)
    }

    override suspend fun insert(customer: CustomerEntity): Int {
        return customerDao.insert(customer).toInt()
    }
}