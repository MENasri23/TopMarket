package ir.jatlin.topmarket.ui.slider

import ir.jatlin.topmarket.core.model.common.ProductImage

sealed interface SliderItem {

    val id: Int

    class ImageItem(val image: ProductImage) : SliderItem {
        override val id: Int get() = image.id
    }
}