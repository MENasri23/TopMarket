package ir.jatlin.topmarket.core.data.mapper

import ir.jatlin.core.database.entity.OrderEntity
import ir.jatlin.core.model.order.Order
import ir.jatlin.core.model.order.OrderLineItem
import ir.jatlin.core.model.order.asOrderStatusName
import ir.jatlin.core.network.model.order.OrderNetwork


fun OrderNetwork.asOrderEntity() = OrderEntity(
    id = id,
    customerId = customerId,
    status = status.asOrderStatusName(),
    totalPrice = totalPrice
)

fun Order.asOrderNetwork() = OrderNetwork(
    id = id,
    customerId = customer.id,
    lineItems = orderItems.map(OrderLineItem::asOrderLineItemNetwork),
    status = status.statusName,
    totalPrice = totalPrice
)