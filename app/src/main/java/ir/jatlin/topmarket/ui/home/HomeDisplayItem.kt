package ir.jatlin.topmarket.ui.home

import androidx.annotation.StringRes
import ir.jatlin.topmarket.ui.product.ProductDisplayItem
import java.util.*


sealed interface HomeDisplayItem {
    val id: String

    class ProductDisplayGroupItem(
        @StringRes val label: Int,
        val data: List<ProductDisplayItem>,
        override val id: String = UUID.randomUUID().toString()
    ) : HomeDisplayItem


}
