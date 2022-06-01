package ir.jatlin.topmarket.core.model.user

import ir.jatlin.topmarket.core.network.model.costumer.ShippingNetwork

data class Shipping(
    val firstName: kotlin.String,
    val postcode: kotlin.String,
    val address1: kotlin.String,
    val address2: kotlin.String,
    val city: kotlin.String,
    val company: kotlin.String,
    val country: kotlin.String,
    val state: kotlin.String,
)

fun ShippingNetwork.asShipping() = Shipping(
    firstName = firstName,
    postcode = postcode,
    address1 = address1,
    address2 = address2,
    city = city,
    company = company,
    country = country,
    state = state,
)