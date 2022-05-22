package ir.jatlin.topmarket.ui.home

import ir.jatlin.topmarket.core.network.model.product.NetworkProduct

sealed interface HomeItem {
    val id: Int

    class ProductItem(val data: NetworkProduct) : HomeItem {
        override val id: Int
            get() = data.id
    }


}