package ir.jatlin.topmarket.core.data.mapper

import ir.jatlin.topmarket.core.model.user.Shipping
import ir.jatlin.topmarket.core.network.model.costumer.ShippingNetwork

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

fun Shipping.asShippingNetwork() = ShippingNetwork(
    firstName = firstName,
    postcode = postcode,
    address1 = address1,
    address2 = address2,
    city = city,
    company = company,
    country = country,
    state = state,
)