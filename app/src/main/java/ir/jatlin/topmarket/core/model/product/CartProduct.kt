package ir.jatlin.topmarket.core.model.product

data class CartProduct(
    val productId: Int,
    val productName: String,
    val weight: String,
    val orderLineId: Int,
    val quantity: Int,
    val totalPrice: String,
    val regularPrice: String,
    val url: String?
)