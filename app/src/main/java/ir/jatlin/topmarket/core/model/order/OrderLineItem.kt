package ir.jatlin.topmarket.core.model.order

data class OrderLineItem(
    val id: Int,
    val productId: Int,
    val productName: String,
    val totalPrice: String,
    val quantity: Int,
)