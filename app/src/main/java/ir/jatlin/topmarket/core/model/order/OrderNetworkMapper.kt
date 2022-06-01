package ir.jatlin.topmarket.core.model.order

import ir.jatlin.topmarket.core.network.model.order.OrderLineItemNetwork
import ir.jatlin.topmarket.core.network.model.order.OrderNetwork

data class Order(
	val billing: ir.jatlin.topmarket.core.network.model.user.BillingNetwork,
	val lineItems: kotlin.collections.List<OrderLineItemNetwork>,
	val paymentMethod: String,
	val paymentMethod_title: String,
	val setPaid: kotlin.Boolean,
	val shipping: ir.jatlin.topmarket.core.network.model.user.ShippingNetwork,
)

fun OrderNetwork.asOrder() = Order(
billing = billing,
lineItems = lineItems,
paymentMethod = paymentMethod,
paymentMethod_title = paymentMethod_title,
setPaid = setPaid,
shipping = shipping,
)