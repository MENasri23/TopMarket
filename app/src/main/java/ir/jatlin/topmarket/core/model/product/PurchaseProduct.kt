package ir.jatlin.topmarket.core.model.product


data class PurchaseProduct(
    val id: Int,
    val productId: Int,
    val productName: String,
    val productPrice: String,
    val count: Int,
)