package ir.jatlin.topmarket.core.model.order

import ir.jatlin.topmarket.core.network.model.order.OrderLineItemNetwork

data class Order(
    val id: Int,
    val lineItems: List<OrderLineItemNetwork>,
    val paymentMethod: String,
    val paymentMethodTitle: String,
    val setPaid: Boolean,
)