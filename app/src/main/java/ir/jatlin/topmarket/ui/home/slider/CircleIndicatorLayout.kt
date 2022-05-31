package ir.jatlin.topmarket.ui.home.slider

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.viewpager.widget.ViewPager
import ir.jatlin.topmarket.R

class CircleIndicatorLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(
    context, attrs, defStyleAttr
) {

    private var viewPager: ViewPager? = null

    private var activePosition = NO_POSITION

    var activeIndicatorRadius: Float = 0f

    var activeIndicatorColor: Int = 0

    var inActiveIndicatorRadius: Float = 0f
    var inActiveIndicatorColor: Int = 0

    var spaceBetween: Int = 0

    var maxVisibleIndicators: Int = 0

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.CircleIndicatorLayout,
            0, 0
        ).apply {

            try {
                activeIndicatorRadius =
                    getDimensionPixelSize(
                        R.styleable.CircleIndicatorLayout_activeIndicatorRadius, 0
                    ).toFloat()
                inActiveIndicatorRadius =
                    getDimensionPixelSize(
                        R.styleable.CircleIndicatorLayout_inActiveIndicatorRadius, 0
                    ).toFloat()

                val activeColor = getColor(
                    R.styleable.CircleIndicatorLayout_activeIndicatorColor,
                    0
                )
                if (activeColor > 0) {
                    activeIndicatorColor = activeColor
                }

                val inActiveColor = getColor(
                    R.styleable.CircleIndicatorLayout_inActiveIndicatorRadius,
                    -1
                )
                if (inActiveColor > 0) {
                    inActiveIndicatorColor = inActiveColor
                }

                val iMargin = getInteger(
                    R.styleable.CircleIndicatorLayout_indicatorMargin,
                    -1
                )
                if (iMargin > 0) {
                    indicatorMargin = iMargin
                }

            } finally {
                recycle()
            }
        }

    }

    private var indicatorMargin = spaceBetween / 2


    fun attachToViewPager(pager: ViewPager?) {
        viewPager = pager ?: return
        showIndicators()
        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    private fun showIndicators() {
        val pager = viewPager ?: return
        activePosition = NO_POSITION

        val adapter = pager.adapter
        if (adapter == null || adapter.count == 0) return

        createIndicators(adapter.count, pager.currentItem)

    }

    private fun createIndicators(count: Int, activePosition: Int) {
        this.activePosition = activePosition
        val extraIndicatorsCount = childCount - count
        when {
            extraIndicatorsCount > 0 -> removeViews(count, extraIndicatorsCount)
            extraIndicatorsCount < 0 -> addIndicators(-extraIndicatorsCount)
        }

    }

    private fun addIndicators(requireCount: Int) {
        repeat(requireCount) {
            addNewIndicator()
        }
    }

    private fun addNewIndicator() {
        val indicator = CircleIndicatorView(context)
        val params = generateDefaultLayoutParams().apply {
            width = indicator.width
            height = indicator.height

            val isFirst = childCount == 0
            marginStart = if (isFirst) 0 else indicatorMargin
        }

        addView(indicator, params)
    }

    companion object {
        const val NO_POSITION = -1
    }

}