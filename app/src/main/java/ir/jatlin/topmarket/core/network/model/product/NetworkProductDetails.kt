package ir.jatlin.topmarket.core.network.model.product

import com.google.gson.annotations.SerializedName
import ir.jatlin.topmarket.core.network.model.common.NetworkImage
import ir.jatlin.topmarket.core.network.model.product.attirbute.NetworkDefaultAttribute
import ir.jatlin.topmarket.core.network.model.product.attirbute.NetworkProductAttribute
import ir.jatlin.topmarket.core.network.model.product.category.NetworkCategory

data class NetworkProductDetails(
    @SerializedName("attributes")
    val attributes: List<NetworkProductAttribute>,
    @SerializedName("average_rating")
    val averageRating: String,
    val categories: List<NetworkCategory>,
    @SerializedName("date_created_gmt")
    val dateCreatedGmt: String,
    @SerializedName("default_attributes")
    val defaultAttributes: List<NetworkDefaultAttribute>,
    val description: String,
    val id: Int,
    val images: List<NetworkImage>,
    val name: String,
    val price: String,
    val purchasable: Boolean,
    @SerializedName("rating_count")
    val ratingCount: Int,
    @SerializedName("regular_price")
    val regularPrice: String,
    @SerializedName("related_ids")
    val relatedIds: List<Int>,
    @SerializedName("total_sales")
    val totalSales: Int,
    @SerializedName("stock_quantity")
    val stockQuantity: Int,
    val weight: String,
    @SerializedName("stock_status")
    val stockStatus: String
)