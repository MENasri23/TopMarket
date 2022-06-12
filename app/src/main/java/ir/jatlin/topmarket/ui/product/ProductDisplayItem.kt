package ir.jatlin.topmarket.ui.product

import ir.jatlin.topmarket.core.model.product.Product


sealed interface ProductDisplayItem {
    val id: Int

    class ProductItem(val data: Product) : ProductDisplayItem {
        override val id: Int get() = data.id
    }

    object ShowMoreItem : ProductDisplayItem {
        override val id: Int
            get() = Int.MIN_VALUE
    }
}

fun Product.asProductItem() =
    ProductDisplayItem.ProductItem(this)