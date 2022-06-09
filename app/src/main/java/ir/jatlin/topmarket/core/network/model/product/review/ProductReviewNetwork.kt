package ir.jatlin.topmarket.core.network.model.product.review

import com.google.gson.annotations.SerializedName

data class ProductReviewNetwork(
    val id: Int,
    @SerializedName("date_created_gmt")
    val dateCreatedGmt: String,
    @SerializedName("product_id")
    val productId: Int,
    val rating: Int,
    @SerializedName("review")
    val content: String,
    @SerializedName("reviewer")
    val reviewerName: String,
    @SerializedName("reviewer_email")
    val reviewerEmail: String,
    val status: String,
    val verified: Boolean
)