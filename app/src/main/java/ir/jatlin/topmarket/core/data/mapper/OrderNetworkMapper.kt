package ir.jatlin.topmarket.core.data.mapper

import ir.jatlin.topmarket.core.model.order.Order
import ir.jatlin.topmarket.core.network.model.order.OrderNetwork


fun OrderNetwork.asOrder() = Order(
    id = id,
    lineItems = lineItems,
    paymentMethod = paymentMethod,
    paymentMethodTitle = paymentMethodTitle,
    setPaid = setPaid,
)

fun Order.asOrderNetwork() = OrderNetwork(
    id = id,
    lineItems = lineItems,
    paymentMethod = paymentMethod,
    paymentMethodTitle = paymentMethodTitle,
    setPaid = setPaid,
)