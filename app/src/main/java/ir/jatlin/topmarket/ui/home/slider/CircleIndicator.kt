package ir.jatlin.topmarket.ui.home.slider

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import ir.jatlin.topmarket.R

class CircleIndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val shapePaint = Paint(Paint.ANTI_ALIAS_FLAG)

    var radius: Float

    init {
        context.obtainStyledAttributes(
            attrs, R.styleable.CircleIndicatorView, defStyleAttr, defStyleRes
        ).apply {

            try {
                radius = getDimensionPixelSize(
                    R.styleable.CircleIndicatorView_radius,
                    NO_RADIUS
                ).toFloat()

            } finally {
                recycle()
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas ?: return
        canvas.drawCircle(x, y, radius, shapePaint)
    }

    companion object {
        private const val NO_RADIUS = 0
    }

}