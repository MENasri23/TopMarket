package ir.jatlin.topmarket.core.model.order

import ir.jatlin.topmarket.core.network.model.order.OrderLineItemNetwork
import ir.jatlin.topmarket.core.network.model.order.OrderNetwork

data class Order(
    val id: Int,
    val billing: ir.jatlin.topmarket.core.network.model.costumer.BillingNetwork,
    val lineItems: kotlin.collections.List<OrderLineItemNetwork>,
    val paymentMethod: String,
    val paymentMethod_title: String,
    val setPaid: kotlin.Boolean,
    val shipping: ir.jatlin.topmarket.core.network.model.costumer.ShippingNetwork,
)