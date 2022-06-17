package ir.jatlin.core.data.source.remote.order

import ir.jatlin.core.data.source.remote.ResponseConverter
import ir.jatlin.core.network.api.MarketApi
import ir.jatlin.core.network.model.order.OrderNetwork
import javax.inject.Inject

class OrderRetrofitDataSource @Inject constructor(
    private val marketApi: MarketApi,
    private val convertResponse: ResponseConverter
) : OrderRemoteDataSource {

    override suspend fun findOrderById(orderId: Int): OrderNetwork {
        return convertResponse(marketApi.getOrder(orderId))
    }

    override suspend fun createOrder(orderNetwork: OrderNetwork): OrderNetwork {
        return convertResponse(marketApi.createOrder(orderNetwork))
    }

    override suspend fun createEmptyOrder(): OrderNetwork {
        return convertResponse(marketApi.createOrder())
    }

    override suspend fun updateOrder(order: OrderNetwork): OrderNetwork {
        return convertResponse(
            marketApi.updateOrder(
                id = order.id,
                order = order
            )
        )
    }
}