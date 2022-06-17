package ir.jatlin.topmarket.ui.home.amazingitem

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import ir.jatlin.core.model.product.Product
import ir.jatlin.topmarket.R

sealed interface AmazingDisplayItem {

    val id: Int

    data class Header(
        // TODO: Change the type of this properties
        //  if the service provide the same functionality
        @StringRes val label: Int = 0,
        @DrawableRes val shapeIcon: Int = 0,
        @DrawableRes val titleIcon: Int = R.drawable.ic_amazing_suggestion
    ) : AmazingDisplayItem {
        override val id: Int = Int.MIN_VALUE + 1
    }

    data class AmazingItem(val product: Product) : AmazingDisplayItem {

        override val id: Int
            get() = product.id
    }

    object ShowMore : AmazingDisplayItem {
        override val id: Int
            get() = Int.MIN_VALUE
    }

}

fun Product.asAmazingItem() = AmazingDisplayItem.AmazingItem(this)