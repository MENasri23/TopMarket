package ir.jatlin.core.model.product

import ir.jatlin.core.model.attirbute.DefaultAttribute
import ir.jatlin.core.model.attirbute.ProductAttribute
import ir.jatlin.core.model.category.Category
import ir.jatlin.core.model.common.ProductImage

data class ProductDetails(
    val attributes: List<ProductAttribute>,
    val averageRating: String,
    val categories: List<Category>,
    val dateCreatedGmt: String,
    val defaultAttributes: List<DefaultAttribute>,
    val description: String,
    val id: Int,
    val images: List<ProductImage>,
    val name: String,
    val price: String,
    val purchasable: Boolean,
    val ratingCount: Int,
    val regularPrice: String,
    val relatedIds: List<Int>,
    val totalSales: Int,
    val stockQuantity: Int,
    val weight: String,
    val stockStatus: String
)