package ir.jatlin.topmarket.core.data.mapper

import ir.jatlin.topmarket.core.model.order.OrderLineItem
import ir.jatlin.topmarket.core.network.model.order.OrderLineItemNetwork


fun OrderLineItemNetwork.asOrderLineItem() = OrderLineItem(
    productId = productId,
    quantity = quantity,
    id = id,
    name = name
)

fun OrderLineItem.asOrderLineItemNetwork() = OrderLineItemNetwork(
    productId = productId,
    quantity = quantity,
    id = id,
    name = name
)