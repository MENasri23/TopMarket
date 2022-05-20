package ir.jatlin.topmarket.core.network.model.common

import com.google.gson.annotations.SerializedName

data class NetworkImage(
    val id: Int,
    val name: String,
    @SerializedName("src")
    val url: String
)