package ir.jatlin.topmarket.core.data.source.remote.model

import ir.jatlin.webservice.model.response.NetworkError


data class Error(
    val code: Int,
    val message: String? = null
)

fun NetworkError.asError() = Error(
    code = code,
    message = message
)