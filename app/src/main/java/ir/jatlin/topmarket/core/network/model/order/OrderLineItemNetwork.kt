package ir.jatlin.topmarket.core.network.model.order

import com.google.gson.annotations.SerializedName
import ir.jatlin.topmarket.core.network.serialization.ExcludeInSerialization

data class OrderLineItemNetwork(
    val id: Int,
    @ExcludeInSerialization
    val name: String,
    @SerializedName("product_id")
    val productId: Int,
    @ExcludeInSerialization
    @SerializedName("total")
    val totalPrice: String,
    val quantity: Int,
)