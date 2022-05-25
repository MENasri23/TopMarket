package ir.jatlin.topmarket.ui.home.category

import ir.jatlin.topmarket.core.network.model.product.NetworkProduct

sealed interface CategoryItem {
    val id: Int

    class ProductItem(val data: NetworkProduct) : CategoryItem {
        override val id: Int
            get() = data.id
    }

}

fun NetworkProduct.asProductItem() =
    CategoryItem.ProductItem(this)