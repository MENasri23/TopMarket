package ir.jatlin.topmarket.core.network.model.product

import com.google.gson.annotations.SerializedName
import ir.jatlin.topmarket.core.network.model.common.NetworkImage
import ir.jatlin.topmarket.core.network.model.product.category.NetworkCategory

data class NetworkProduct(
    val categories: List<NetworkCategory>,
    val id: Int,
    val images: List<NetworkImage>,
    val name: String,
    val price: String,
    val description: String,
    @SerializedName("regular_price")
    val regularPrice: String,
    @SerializedName("stock_status")
    val stockStatus: String, // instock  outofstock onbackorder
)