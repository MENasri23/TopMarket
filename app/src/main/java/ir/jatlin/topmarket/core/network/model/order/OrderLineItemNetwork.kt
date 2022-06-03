package ir.jatlin.topmarket.core.network.model.order

import com.google.gson.annotations.SerializedName

data class OrderLineItemNetwork(
    val id: Int,
    val name: String,
    @SerializedName("quantity")
    val productId: Int,
    val quantity: Int,
)