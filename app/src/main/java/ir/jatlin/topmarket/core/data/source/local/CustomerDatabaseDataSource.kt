package ir.jatlin.topmarket.core.data.source.local

import ir.jatlin.topmarket.core.data.mapper.asCustomerNetwork
import ir.jatlin.topmarket.core.database.dao.CustomerDao
import ir.jatlin.topmarket.core.database.entity.CustomerEntity
import ir.jatlin.topmarket.core.database.entity.CustomerWithOrders
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

    override suspend fun save(customer: CustomerEntity): Int {
        return customerDao.insert(customer).toInt()
    }
}