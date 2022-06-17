package ir.jatlin.core.data.mapper

import ir.jatlin.core.database.entity.CustomerEntity
import ir.jatlin.core.model.user.Customer
import ir.jatlin.core.network.model.costumer.CustomerNetwork


fun CustomerNetwork.asCustomerEntity() = CustomerEntity(
    id = id,
    email = email,
    username = username,
    firstName = firstName,
    lastName = lastName,
    avatarUrl = avatar_url,
    role = role,
)

fun Customer.asCustomerNetwork() = CustomerNetwork(
    id = id,
    email = email,
    username = username,
    firstName = firstName,
    lastName = lastName,
    avatar_url = avatarUrl,
    role = role
)