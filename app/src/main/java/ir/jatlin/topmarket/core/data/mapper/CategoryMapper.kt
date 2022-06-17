package ir.jatlin.topmarket.core.data.mapper

import ir.jatlin.core.model.category.Category
import ir.jatlin.core.model.category.CategoryDetails
import ir.jatlin.core.network.model.product.category.NetworkCategory
import ir.jatlin.core.network.model.product.category.NetworkCategoryDetails


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