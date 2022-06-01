package ir.jatlin.topmarket.core.model.order

data class OrderLineItem(
	val productId: kotlin.Int,
	val quantity: kotlin.Int,
	val variationId: kotlin.Int,
)