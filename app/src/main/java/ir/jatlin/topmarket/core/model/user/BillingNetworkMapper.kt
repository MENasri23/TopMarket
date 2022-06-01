package ir.jatlin.topmarket.core.model.user

import ir.jatlin.topmarket.core.network.model.costumer.BillingNetwork

data class Billing(
    val address1: kotlin.String,
    val address2: kotlin.String,
    val city: kotlin.String,
    val company: kotlin.String,
    val country: kotlin.String,
    val email: kotlin.String,
    val firstName: kotlin.String,
    val lastName: kotlin.String,
    val phone: kotlin.String,
    val postcode: kotlin.String,
    val state: kotlin.String,
)

fun BillingNetwork.asBilling() = Billing(
    address1 = address1,
    address2 = address2,
    city = city,
    company = company,
    country = country,
    email = email,
    firstName = firstName,
    lastName = lastName,
    phone = phone,
    postcode = postcode,
    state = state,
)