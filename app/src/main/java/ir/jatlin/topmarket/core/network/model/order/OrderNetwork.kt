package ir.jatlin.topmarket.core.network.model.order

import com.google.gson.annotations.SerializedName

data class OrderNetwork(
    val id: Int,
    @SerializedName("line_items")
    val lineItems: List<OrderLineItemNetwork>,
    @SerializedName("payment_method")
    val paymentMethod: String,
    @SerializedName("payment_method_title")
    val paymentMethodTitle: String,
    @SerializedName("set_paid")
    val setPaid: Boolean,
)