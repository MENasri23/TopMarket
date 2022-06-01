package ir.jatlin.topmarket.core.model.order

import ir.jatlin.topmarket.core.network.model.order.OrderLineItemNetwork

data class OrderLineItem(
	val productId: kotlin.Int,
	val quantity: kotlin.Int,
	val variationId: kotlin.Int,
)

fun OrderLineItemNetwork.asOrderLineItem() = OrderLineItem(
productId = productId,
quantity = quantity,
variationId = variationId,
)