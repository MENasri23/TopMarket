package ir.jatlin.topmarket.core.model.category

import ir.jatlin.topmarket.core.model.common.ProductImage

data class CategoryDetails(
    val productCount: Int,
    val description: String,
    val id: Int,
    val image: ProductImage?,
    val name: String,
    val parentId: Int
)