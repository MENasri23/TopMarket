package ir.jatlin.topmarket.core.model.order

import ir.jatlin.topmarket.core.model.user.Customer

data class Order(
    val id: Int,
    val orderItems: List<OrderLineItem>,
    val customer: Customer,
    val status: OrderStatus
)