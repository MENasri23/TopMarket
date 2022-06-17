package ir.jatlin.core.network.model.product.category

import com.google.gson.annotations.SerializedName
import ir.jatlin.core.network.model.common.NetworkImage

data class NetworkCategoryDetails(
    @SerializedName("count")
    val productCount: Int,
    val description: String,
    val id: Int,
    val image: NetworkImage?,
    val name: String,
    @SerializedName("parent")
    val parentId: Int
)