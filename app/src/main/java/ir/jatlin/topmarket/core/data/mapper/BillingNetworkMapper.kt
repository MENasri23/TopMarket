package ir.jatlin.topmarket.core.data.mapper

import ir.jatlin.topmarket.core.model.user.Billing
import ir.jatlin.topmarket.core.network.model.costumer.BillingNetwork

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

fun Billing.asBillingNetwork() = BillingNetwork(
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