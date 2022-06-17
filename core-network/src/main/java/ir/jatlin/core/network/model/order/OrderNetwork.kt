package ir.jatlin.core.network.model.order

import com.google.gson.annotations.SerializedName

data class OrderNetwork(
    val id: Int,
    @SerializedName("line_items")
    val lineItems: List<OrderLineItemNetwork>,
    @SerializedName("customer_id")
    val customerId: Int,
    val status: String,
    @SerializedName("total")
    val totalPrice: String
)