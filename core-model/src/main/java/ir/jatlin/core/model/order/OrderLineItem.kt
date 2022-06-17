package ir.jatlin.core.model.order

data class OrderLineItem(
    val id: Int,
    val productId: Int,
    val productName: String,
    val totalPrice: String,
    val quantity: Int,
) {
    companion object {
        val Empty = OrderLineItem(
            id = 0,
            productId = 0,
            productName = "",
            totalPrice = "",
            quantity = 0
        )
    }
}