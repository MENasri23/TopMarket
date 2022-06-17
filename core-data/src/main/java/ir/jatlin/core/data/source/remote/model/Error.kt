package ir.jatlin.core.data.source.remote.model

import ir.jatlin.core.network.response.NetworkError
import ir.jatlin.core.network.response.NetworkStatus


data class Error(
    val identity: String,
    val message: String,
    val status: Status
)

data class Status(
    val code: Int
)

fun NetworkError.asError() = Error(
    identity = identity,
    message = message,
    status = networkStatus.asStatus()
)

fun NetworkStatus.asStatus() = Status(code)