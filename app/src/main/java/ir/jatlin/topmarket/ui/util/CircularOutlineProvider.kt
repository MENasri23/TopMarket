package ir.jatlin.topmarket.ui.util

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider

object CircularOutlineProvider : ViewOutlineProvider() {
    override fun getOutline(view: View, outline: Outline) {
        val radius = minOf(view.width, view.height) / 2f
        outline.setRoundRect(
            0,
            0,
            view.width,
            view.height,
            radius
        )
    }
}
