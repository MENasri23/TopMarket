package ir.jatlin.topmarket.core.data.mapper

import ir.jatlin.topmarket.core.model.order.Order
import ir.jatlin.topmarket.core.network.model.order.OrderLineItemNetwork
import ir.jatlin.topmarket.core.network.model.order.OrderNetwork


fun OrderNetwork.asOrder() = Order(
    id = id,
    billing = billing,
    lineItems = lineItems,
    paymentMethod = paymentMethod,
    paymentMethod_title = paymentMethod_title,
    setPaid = setPaid,
    shipping = shipping,
)

fun Order.asOrderNetwork() = OrderNetwork(
    id = id,
    billing = billing,
    lineItems = lineItems,
    paymentMethod = paymentMethod,
    paymentMethod_title = paymentMethod_title,
    setPaid = setPaid,
    shipping = shipping,
)