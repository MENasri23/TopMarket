package ir.jatlin.core.network.model.order

import com.google.gson.annotations.SerializedName
import ir.jatlin.core.network.serialization.ExcludeInSerialization

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