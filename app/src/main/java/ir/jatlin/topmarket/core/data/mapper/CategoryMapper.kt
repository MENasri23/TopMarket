package ir.jatlin.topmarket.core.data.mapper

import ir.jatlin.topmarket.core.model.category.Category
import ir.jatlin.topmarket.core.model.category.CategoryDetails
import ir.jatlin.topmarket.core.network.model.product.category.NetworkCategory
import ir.jatlin.topmarket.core.network.model.product.category.NetworkCategoryDetails


fun NetworkCategory.asCategory() = Category(
    id = id,
    name = name,
)

fun NetworkCategoryDetails.asCategoryDetails() = CategoryDetails(
    productCount = productCount,
    description = description,
    id = id,
    image = image?.asProductImage(),
    name = name,
    parentId = parentId,
)