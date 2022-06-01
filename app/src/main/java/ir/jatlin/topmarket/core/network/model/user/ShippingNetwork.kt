package ir.jatlin.topmarket.core.network.model.user

import com.google.gson.annotations.SerializedName

data class ShippingNetwork(
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val postcode: String,
    @SerializedName("address_1")
    val address1: String,
    @SerializedName("address_2")
    val address2: String,
    val city: String,
    val company: String,
    val country: String,
    val state: String
)