package ir.jatlin.topmarket.ui.product

import ir.jatlin.topmarket.core.network.model.product.NetworkProduct


sealed interface ProductDisplayItem {
    val id: Int

    class ProductItem(val data: NetworkProduct) : ProductDisplayItem {
        override val id: Int get() = data.id
    }

    object ShowMoreItem : ProductDisplayItem {
        override val id: Int
            get() = Int.MIN_VALUE
    }
}

fun NetworkProduct.asProductItem() =
    ProductDisplayItem.ProductItem(this)