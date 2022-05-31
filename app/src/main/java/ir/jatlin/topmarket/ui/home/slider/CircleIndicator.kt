package ir.jatlin.topmarket.ui.home.slider

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import ir.jatlin.topmarket.R
import kotlin.math.min

class CircleIndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {

    var radius: Float = 0f
    var color: Int = Color.TRANSPARENT

    init {
        context.obtainStyledAttributes(
            attrs, R.styleable.CircleIndicatorView, defStyleAttr, defStyleRes
        ).apply {

            try {
                val tRadius = getDimensionPixelSize(
                    R.styleable.CircleIndicatorView_radius,
                    0
                )
                if (tRadius >= 0) {
                    radius = tRadius.toFloat()
                }

                color = getColor(
                    R.styleable.CircleIndicatorView_color,
                    0
                )

            } finally {
                recycle()
            }
        }
    }

    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = this@CircleIndicatorView.color
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val circleSize = (2 * radius + 1f).toInt()
        val minW =
            paddingLeft + paddingRight + min(circleSize, suggestedMinimumWidth)
        val width = resolveSize(minW, widthMeasureSpec)

        val minH =
            paddingTop + paddingBottom + min(circleSize, suggestedMinimumHeight)
        val height = resolveSize(minH, heightMeasureSpec)

        setMeasuredDimension(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas ?: return
        canvas.drawCircle(x, y, radius, circlePaint)
    }


}