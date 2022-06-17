package ir.jatlin.core.data.mapper

import ir.jatlin.core.database.entity.OrderLineItemEntity
import ir.jatlin.core.model.order.OrderLineItem
import ir.jatlin.core.network.model.order.OrderLineItemNetwork


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