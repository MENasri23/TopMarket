package ir.jatlin.topmarket.core.network.model.costumer

import com.google.gson.annotations.SerializedName

data class CustomerNetwork(
    val id: Int,
    val email: String,
    val username: String,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("avatar_url")
    val avatar_url: String,
    val role: String
)