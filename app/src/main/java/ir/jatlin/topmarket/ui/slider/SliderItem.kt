package ir.jatlin.topmarket.ui.slider

import ir.jatlin.core.model.common.ProductImage

sealed interface SliderItem {

    val id: Int

    class ImageItem(val image: ProductImage) : SliderItem {
        override val id: Int get() = image.id
    }
}