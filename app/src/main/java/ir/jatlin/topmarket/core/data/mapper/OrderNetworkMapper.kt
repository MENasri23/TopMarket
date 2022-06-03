package ir.jatlin.topmarket.core.data.mapper

import ir.jatlin.topmarket.core.database.entity.OrderEntity
import ir.jatlin.topmarket.core.model.order.Order
import ir.jatlin.topmarket.core.model.order.OrderLineItem
import ir.jatlin.topmarket.core.model.order.asOrderStatusName
import ir.jatlin.topmarket.core.network.model.order.OrderNetwork


fun OrderNetwork.asOrderEntity() = OrderEntity(
    id = id,
    customerId = customerId,
    status = status.asOrderStatusName()
)

fun Order.asOrderNetwork() = OrderNetwork(
    id = id,
    customerId = customer.id,
    lineItems = orderItems.map(OrderLineItem::asOrderLineItemNetwork),
    status = status.statusName
)