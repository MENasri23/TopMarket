package ir.jatlin.topmarket.core.network.model.order

import com.google.gson.annotations.SerializedName

data class OrderLineItemNetwork(
    @SerializedName("quantity")
    val productId: Int,
    val quantity: Int,
    @SerializedName("variation_id")
    val variationId: Int
)