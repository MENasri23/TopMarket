package ir.jatlin.topmarket.core.network.model.product.category

import com.google.gson.annotations.SerializedName
import ir.jatlin.topmarket.core.network.model.common.NetworkImage

data class NetworkCategoryDetails(
    @SerializedName("count")
    val productCount: Int,
    val description: String,
    val display: String, // default, products, subcategories
    val id: Int,
    val image: NetworkImage?,
    @SerializedName("menu_order")
    val menuOrder: Int,
    val name: String,
    @SerializedName("parent")
    val parentId: Int,
    val slug: String
)