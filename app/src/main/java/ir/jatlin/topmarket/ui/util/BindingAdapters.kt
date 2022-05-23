package ir.jatlin.topmarket.ui.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import ir.jatlin.topmarket.R
import timber.log.Timber
import java.lang.NumberFormatException


fun ImageView.loadFromUrl(
    url: String?,
    @DrawableRes placeholder: Int = R.drawable.loading_animation
) {
    Glide.with(context)
        .load(url)
        .apply(
            RequestOptions()
                .placeholder(placeholder)
                .error(R.drawable.ic_broken_image)
        )
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

fun View.clipToRoundRect(clip: Boolean) {
    clipToOutline = clip
    outlineProvider = if (clip) RoundedOutlineProvided else null
}


fun TextView.setDiscount(
    beforeDiscount: String,
    afterDiscount: String
) {
    val discount = percentDiscount(beforeDiscount, afterDiscount)
    Timber.d("*****************************$discount")
    val isVisible = if (discount != null) {
        text = context.getString(
            R.string.price_discount, discount
        )
        View.VISIBLE
    } else View.INVISIBLE

    visibility = isVisible

}


fun withSeparator(origin: String, separator: Char = ',') = buildString {
    var i = origin.lastIndex
    while (i > 2) {
        append(origin[i--])
        append(origin[i--])
        append(origin[i--])
        append(separator)
    }
    while (i >= 0) append(origin[i--])

    reverse()
}

private fun percentDiscount(
    beforeDiscount: String, afterDiscount: String
): Int? {
    return try {

        val before = beforeDiscount.toInt()
        val after = afterDiscount.toInt()

        Timber.d("regularPrice: $before, price: $after")

        if (before <= after) null
        else ((before - after) * 100) / before

    } catch (e: NumberFormatException) {
        Timber.d(
            "Unresolved number format for values: $beforeDiscount, $afterDiscount"
        )
        null
    }
}