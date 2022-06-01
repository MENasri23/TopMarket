package ir.jatlin.topmarket.core.model.user

import ir.jatlin.topmarket.core.network.model.costumer.BillingNetwork
import ir.jatlin.topmarket.core.network.model.costumer.ShippingNetwork

data class Customer(
    val id: kotlin.Int,
    val email: kotlin.String,
    val username: kotlin.String,
    val firstName: kotlin.String,
    val lastName: kotlin.String,
    val avatar_url: kotlin.String,
    val billing: BillingNetwork,
    val shipping: ShippingNetwork,
    val isPayingCustomer: kotlin.Boolean,
    val role: kotlin.String,
    val dateCreatedGmt: kotlin.String,
    val dateModifiedGmt: kotlin.String,
)