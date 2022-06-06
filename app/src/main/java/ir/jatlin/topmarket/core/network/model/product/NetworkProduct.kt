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
    val stockStatus: String,
    // TODO: These property maybe removed in future
    @SerializedName("date_created_gmt")
    val createdDateGmt: String,
    @SerializedName("date_created")
    val createdDate: String,
    @SerializedName("rating_count")
    val ratingCount: Int,
    @SerializedName("total_sales")
    val totalSales: Int,
    @SerializedName("average_rating")
    val averageRating: String
)