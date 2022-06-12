package ir.jatlin.topmarket.core.model.product

import ir.jatlin.topmarket.core.model.category.Category
import ir.jatlin.topmarket.core.model.common.ProductImage

data class Product(
    val categories: List<Category>,
    val id: Int,
    val images: List<ProductImage>,
    val name: String,
    val price: String,
    val description: String,
    val regularPrice: String,
    val stockStatus: String,
    val createdDateGmt: String,
    val createdDate: String,
    val ratingCount: Int,
    val totalSales: Int,
    val averageRating: String
)