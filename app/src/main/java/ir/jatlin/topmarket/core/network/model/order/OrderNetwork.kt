package ir.jatlin.topmarket.core.network.model.order

import com.google.gson.annotations.SerializedName
import ir.jatlin.topmarket.core.network.model.costumer.BillingNetwork
import ir.jatlin.topmarket.core.network.model.costumer.ShippingNetwork

data class OrderNetwork(
    val id: Int,
    val billing: BillingNetwork,
    @SerializedName("line_items")
    val lineItems: List<OrderLineItemNetwork>,
    @SerializedName("payment_method")
    val paymentMethod: String,
    @SerializedName("payment_method_title")
    val paymentMethod_title: String,
    @SerializedName("set_paid")
    val setPaid: Boolean,
    val shipping: ShippingNetwork
)