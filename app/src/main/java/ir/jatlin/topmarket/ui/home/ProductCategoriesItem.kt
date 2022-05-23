package ir.jatlin.topmarket.ui.home

import ir.jatlin.topmarket.ui.home.category.ProductCategoryItem
import java.util.*

sealed interface ProductCategoriesItem {
    val id: String

    class CategoryItem(
        val data: List<ProductCategoryItem>,
        val label: String,
        override val id: String = UUID.randomUUID().toString()
    ) : ProductCategoriesItem

}