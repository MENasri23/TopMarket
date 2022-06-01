package ir.jatlin.topmarket.core.data.mapper

import ir.jatlin.topmarket.core.model.user.Customer
import ir.jatlin.topmarket.core.network.model.costumer.BillingNetwork
import ir.jatlin.topmarket.core.network.model.costumer.CustomerNetwork
import ir.jatlin.topmarket.core.network.model.costumer.ShippingNetwork


fun CustomerNetwork.asCustomer() = Customer(
    id = id,
    email = email,
    username = username,
    firstName = firstName,
    lastName = lastName,
    avatar_url = avatar_url,
    billing = billing,
    shipping = shipping,
    isPayingCustomer = isPayingCustomer,
    role = role,
    dateCreatedGmt = dateCreatedGmt,
    dateModifiedGmt = dateModifiedGmt,
)

fun Customer.asCustomerNetwork() = CustomerNetwork(
    id = id,
    email = email,
    username = username,
    firstName = firstName,
    lastName = lastName,
    avatar_url = avatar_url,
    billing = billing,
    shipping = shipping,
    isPayingCustomer = isPayingCustomer,
    role = role,
    dateCreatedGmt = dateCreatedGmt,
    dateModifiedGmt = dateModifiedGmt,
)