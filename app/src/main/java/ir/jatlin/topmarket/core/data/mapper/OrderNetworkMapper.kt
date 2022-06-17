package ir.jatlin.topmarket.core.data.mapper

import ir.jatlin.core.model.order.Order
import ir.jatlin.core.model.order.OrderLineItem
import ir.jatlin.core.model.order.asOrderStatusName
import ir.jatlin.core.network.model.order.OrderNetwork
import ir.jatlin.topmarket.core.database.entity.OrderEntity


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