package ir.jatlin.topmarket.core.data.mapper

import ir.jatlin.core.model.order.OrderLineItem
import ir.jatlin.core.network.model.order.OrderLineItemNetwork
import ir.jatlin.topmarket.core.database.entity.OrderLineItemEntity


fun OrderLineItemNetwork.asOrderLineItemEntity(orderId: Int) = OrderLineItemEntity(
    productId = productId,
    quantity = quantity,
    id = id,
    productName = name,
    totalPrice = totalPrice,
    orderId = orderId
)

fun OrderLineItem.asOrderLineItemNetwork() = OrderLineItemNetwork(
    productId = productId,
    quantity = quantity,
    id = id,
    name = productName,
    totalPrice = totalPrice
)