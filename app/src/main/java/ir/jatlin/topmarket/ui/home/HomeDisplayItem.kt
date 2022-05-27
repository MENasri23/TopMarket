package ir.jatlin.topmarket.ui.home

import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import ir.jatlin.topmarket.ui.home.amazingitem.AmazingDisplayItem
import ir.jatlin.topmarket.ui.product.ProductDisplayItem
import java.util.*


sealed interface HomeDisplayItem {
    val id: String

    class ProductDisplayGroupItem(
        @StringRes val label: Int,
        val products: List<ProductDisplayItem>,
        override val id: String = UUID.randomUUID().toString()
    ) : HomeDisplayItem


    class AmazingSuggestionGroupItem(
        val suggestionItems: List<AmazingDisplayItem>,
        @ColorInt val backgroundColor: Long = 0xEF3F55,
        override val id: String = UUID.randomUUID().toString()
    ) : HomeDisplayItem


}
