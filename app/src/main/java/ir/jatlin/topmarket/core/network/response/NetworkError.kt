package ir.jatlin.topmarket.core.network.response

import com.google.gson.annotations.SerializedName

data class NetworkError(
    @SerializedName("code")
    val identity: String,
    @SerializedName("status_message")
    val message: String,
    val networkStatus: NetworkStatus
)

data class NetworkStatus(
    @SerializedName("status")
    val code: Int
)