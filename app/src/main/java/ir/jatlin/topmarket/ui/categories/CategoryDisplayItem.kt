package ir.jatlin.topmarket.ui.categories

import androidx.annotation.StringRes
import ir.jatlin.topmarket.core.network.model.product.category.NetworkCategoryDetails
import java.util.*

/**
 * The data holder for category screen
 */
interface CategoryDisplayItem {

    val id: String

    class ProductCategoryGroupItem(
        @StringRes val label: Int,
        val categories: List<ProductCategoryDisplayItem>,
        override val id: String = UUID.randomUUID().toString()
    ) : CategoryDisplayItem


}

sealed interface ProductCategoryDisplayItem {
    val id: Int

    class CategoryItem(val data: NetworkCategoryDetails) : ProductCategoryDisplayItem {
        override val id: Int
            get() = data.id
    }

}

fun NetworkCategoryDetails.asCategoryItem() =
    ProductCategoryDisplayItem.CategoryItem(this)