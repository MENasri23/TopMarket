package ir.jatlin.topmarket.ui.home.category

import ir.jatlin.topmarket.core.network.model.product.NetworkProduct
import ir.jatlin.topmarket.core.network.model.product.category.NetworkCategoryDetails

sealed interface DisplayItem {
    val id: Int

    class ProductItem(val data: NetworkProduct) : DisplayItem {
        override val id: Int
            get() = data.id
    }

    class CategoryItem(val data: NetworkCategoryDetails) : DisplayItem {
        override val id: Int
            get() = data.id
    }


}

fun NetworkProduct.asProductItem() =
    DisplayItem.ProductItem(this)

fun NetworkCategoryDetails.asCategoryItem() =
    DisplayItem.CategoryItem(this)