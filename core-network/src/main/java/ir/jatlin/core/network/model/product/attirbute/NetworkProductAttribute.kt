package ir.jatlin.core.network.model.product.attirbute

import com.google.gson.annotations.SerializedName

data class NetworkProductAttribute(
    @SerializedName("has_archives")
    val hasArchives: Boolean,
    val id: Int,
    val name: String,
    @SerializedName("order_by")
    val orderBy: String,
    val slug: String,
    val type: String
)