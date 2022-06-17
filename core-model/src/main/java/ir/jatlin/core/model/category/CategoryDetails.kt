package ir.jatlin.core.model.category

import ir.jatlin.core.model.common.ProductImage

data class CategoryDetails(
    val productCount: Int,
    val description: String,
    val id: Int,
    val image: ProductImage?,
    val name: String,
    val parentId: Int
)