package ir.jatlin.topmarket.ui.home.amazingitem

import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.core.network.model.product.NetworkProduct

sealed interface AmazingDisplayItem {

    val id: Int

    class Header(
        // TODO: Change the type of this properties
        //  if the service provide the same functionality
        @StringRes val label: Int = 0,
        @DrawableRes val shapeIcon: Int = 0,
        @DrawableRes val titleIcon: Int = R.drawable.ic_amazing_suggestion
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

fun NetworkProduct.asAmazingItem() = AmazingDisplayItem.AmazingItem(this)