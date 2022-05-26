package ir.jatlin.topmarket.ui.slider

import ir.jatlin.topmarket.core.network.model.common.NetworkImage

sealed interface SliderItem {

    val id: Int

    class ImageItem(val image: NetworkImage) : SliderItem {
        override val id: Int get() = image.id
    }
}