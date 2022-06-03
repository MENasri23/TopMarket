package ir.jatlin.topmarket.core.data.mapper

import ir.jatlin.topmarket.core.database.entity.OrderLineItemEntity
import ir.jatlin.topmarket.core.model.order.OrderLineItem
import ir.jatlin.topmarket.core.network.model.order.OrderLineItemNetwork


fun OrderLineItemNetwork.asOrderLineItemEntity() = OrderLineItemEntity(
    productId = productId,
    quantity = quantity,
    id = id,
    productName = name,
    totalPrice = totalPrice,
    orderId = 0 // TODO: Fix it
)

fun OrderLineItem.asOrderLineItemNetwork() = OrderLineItemNetwork(
    productId = productId,
    quantity = quantity,
    id = id,
    name = productName,
    totalPrice = totalPrice
)