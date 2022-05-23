package ir.jatlin.topmarket.ui.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import ir.jatlin.topmarket.R
import timber.log.Timber


fun ImageView.loadFromUrl(
    url: String?,
    @DrawableRes placeholder: Int = R.drawable.loading_animation,

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

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.visible() {
    isVisible = true
}

fun View.gone() {
    isVisible = false
}


fun TextView.setDiscount(
    beforeDiscount: String,
    afterDiscount: String
) {
    val discount = percentDiscount(beforeDiscount, afterDiscount)
    if (discount != null) {
        text = context.getString(
            R.string.price_discount, discount
        )
        visible()
    } else invisible()

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
        when {
            beforeDiscount.length > 10 -> Timber.d("Integer parsing overflow for input: $beforeDiscount")
            afterDiscount.length > 10 -> Timber.d("Integer parsing overflow for input: $beforeDiscount")
            else -> Timber.d("Unresolved number format for values: $beforeDiscount, $afterDiscount")
        }
        null
    }
}