package ir.jatlin.webservice.model.response

import com.google.gson.annotations.SerializedName

data class NetworkError(
    @SerializedName("status_code")
    val code: Int,
    @SerializedName("status_message")
    val message: String
)