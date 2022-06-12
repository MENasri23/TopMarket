package ir.jatlin.topmarket.ui.home

import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import ir.jatlin.topmarket.core.model.common.ProductImage
import ir.jatlin.topmarket.ui.home.amazingitem.AmazingDisplayItem
import ir.jatlin.topmarket.ui.product.ProductDisplayItem
import java.util.*


sealed interface HomeDisplayItem {
    val id: String

    data class ProductDisplayGroupItem(
        @StringRes val label: Int,
        val products: List<ProductDisplayItem>,
        override val id: String = UUID.randomUUID().toString()
    ) : HomeDisplayItem


    data class AmazingSuggestionGroupItem(
        val suggestionItems: List<AmazingDisplayItem>,
        @ColorInt val backgroundColor: Long = 0xEF3F55,
        override val id: String = UUID.randomUUID().toString()
    ) : HomeDisplayItem

    data class SpecialProductsSliderItem(
        val specialProductImages: List<ProductImage>,
        override val id: String = UUID.randomUUID().toString()
    ) : HomeDisplayItem


}
