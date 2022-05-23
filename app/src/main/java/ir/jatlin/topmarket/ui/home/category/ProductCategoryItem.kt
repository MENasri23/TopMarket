package ir.jatlin.topmarket.ui.home.category

import ir.jatlin.topmarket.core.network.model.product.NetworkProduct

sealed interface ProductCategoryItem {
    val id: Int

    class ProductItem(val data: NetworkProduct) : ProductCategoryItem {
        override val id: Int
            get() = data.id
    }

}