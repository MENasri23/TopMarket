package ir.jatlin.topmarket.ui.util

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import ir.jatlin.topmarket.R


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