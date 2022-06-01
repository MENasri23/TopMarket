package ir.jatlin.topmarket.core.data.source.remote.customer

import ir.jatlin.data.source.remote.ResponseConverter
import ir.jatlin.topmarket.core.network.api.MarketApi
import ir.jatlin.topmarket.core.network.model.costumer.CustomerNetwork
import javax.inject.Inject

class CustomerRetrofitDataSource @Inject constructor(
    private val marketApi: MarketApi,
    private val convertResponse: ResponseConverter
) : CustomerRemoteDataSource {

    override suspend fun findCustomerById(customerId: Int): CustomerNetwork {
        return convertResponse(marketApi.getCustomer(customerId))
    }

    override suspend fun createCustomer(customerNetwork: CustomerNetwork): CustomerNetwork {
        return convertResponse((marketApi.createCustomer(customerNetwork)))
    }

    override suspend fun updateCustomer(customer: CustomerNetwork): CustomerNetwork {
        return convertResponse(
            marketApi.updateCustomer(
                id = customer.id,
                customer = customer
            )
        )
    }
}