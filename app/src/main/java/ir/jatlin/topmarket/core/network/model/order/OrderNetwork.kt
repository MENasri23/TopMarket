package ir.jatlin.topmarket.core.network.model.order

import com.google.gson.annotations.SerializedName
import ir.jatlin.topmarket.core.network.model.user.BillingNetwork
import ir.jatlin.topmarket.core.network.model.user.ShippingNetwork

data class OrderNetwork(
    val billing: BillingNetwork,
    @SerializedName("line_items")
    val lineItems: List<OrderLineItem>,
    @SerializedName("payment_method")
    val paymentMethod: String,
    @SerializedName("payment_method_title")
    val paymentMethod_title: String,
    @SerializedName("set_paid")
    val setPaid: Boolean,
    val shipping: ShippingNetwork
)