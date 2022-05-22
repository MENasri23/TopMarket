package ir.jatlin.topmarket.ui.util

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions


fun ImageView.loadFromUrl(url: String?, @DrawableRes placeholder: Int) {
    Glide.with(context)
        .load(url)
        .placeholder(placeholder)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}