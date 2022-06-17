package ir.jatlin.topmarket.ui.categories

import ir.jatlin.core.model.category.CategoryDetails
import java.util.*

/**
 * The data holder for category screen
 */
sealed interface CategoryDisplayItem {

    val id: String

    class ProductCategoryGroupItem(
        val label: String,
        val categories: List<ProductCategoryDisplayItem>,
        override val id: String = UUID.randomUUID().toString()
    ) : CategoryDisplayItem


}

sealed interface ProductCategoryDisplayItem {
    val id: Int

    class CategoryItem(val data: CategoryDetails) : ProductCategoryDisplayItem {
        override val id: Int
            get() = data.id
    }

}

fun CategoryDetails.asCategoryItem() =
    ProductCategoryDisplayItem.CategoryItem(this)