package ir.jatlin.topmarket.ui.home.amazing

import androidx.annotation.DrawableRes
import ir.jatlin.topmarket.core.network.model.product.NetworkProduct

sealed interface AmazingDisplayItem {

    val id: Int

    class Header(
        @DrawableRes val icon: Int
    ) : AmazingDisplayItem {
        override val id: Int = Int.MIN_VALUE + 1
    }

    class AmazingItem(val product: NetworkProduct) : AmazingDisplayItem {

        override val id: Int
            get() = product.id
    }

    object ShowMore : AmazingDisplayItem {
        override val id: Int
            get() = Int.MIN_VALUE
    }

}