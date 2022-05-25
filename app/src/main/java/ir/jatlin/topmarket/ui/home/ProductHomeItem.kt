package ir.jatlin.topmarket.ui.home

import ir.jatlin.topmarket.ui.home.category.CategoryItem
import java.util.*

sealed interface ProductHomeItem {
    val id: String

    class CategoriesItem(
        val data: List<CategoryItem>,
        val label: String,
        override val id: String = UUID.randomUUID().toString()
    ) : ProductHomeItem

}