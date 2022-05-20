package ir.jatlin.topmarket.core.network.model.product

import com.google.gson.annotations.SerializedName
import ir.jatlin.topmarket.core.network.model.product.attirbute.NetworkDefaultAttribute
import ir.jatlin.topmarket.core.network.model.common.NetworkImage
import ir.jatlin.topmarket.core.network.model.product.attirbute.NetworkProductAttribute
import ir.jatlin.topmarket.core.network.model.product.category.NetworkCategory
import ir.jatlin.topmarket.core.network.model.product.tag.NetworkTag

data class NetworkProductDetails(
    @SerializedName("attributes")
    val attributes: List<NetworkProductAttribute>,
    @SerializedName("average_rating")
    val averageRating: String,
    val backordered: Boolean,
    val backorders: String,
    @SerializedName("backorders_allowed")
    val backordersAllowed: Boolean,
    val categories: List<NetworkCategory>,
    @SerializedName("date_created_gmt")
    val dateCreatedGmt: String,
    @SerializedName("date_modified_gmt")
    val dateModifiedGmt: String,
    @SerializedName("default_attributes")
    val defaultAttributes: List<NetworkDefaultAttribute>,
    val description: String,
    val id: Int,
    val images: List<NetworkImage>,
    @SerializedName("menu_order")
    val menuOrder: Int,
    val name: String,
    @SerializedName("on_sale")
    val isOnSale: Boolean,
    @SerializedName("parent_id")
    val parentId: Int,
    @SerializedName("permalink")
    val link: String,
    val price: String,
    val purchasable: Boolean,
    @SerializedName("rating_count")
    val ratingCount: Int,
    @SerializedName("regular_price")
    val regularPrice: String,
    @SerializedName("related_ids")
    val relatedIds: List<Int>,
    val slug: String,
    val tags: List<NetworkTag>,
    @SerializedName("total_sales")
    val totalSales: Int,
    @SerializedName("stock_quantity")
    val stockQuantity: Int,
    @SerializedName("variations")
    val variationIds: List<Any>,
    val weight: String
)