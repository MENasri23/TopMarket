package ir.jatlin.topmarket.ui.home.slider

import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import ir.jatlin.topmarket.databinding.SpecialProductSlideGroupBinding
import ir.jatlin.topmarket.ui.home.HomeDisplayItem
import ir.jatlin.topmarket.ui.viewholder.BaseViewHolder
import timber.log.Timber

class SpecialProductGroupViewHolder(
    binding: SpecialProductSlideGroupBinding,
    private val eventListener: EventListener
) : BaseViewHolder<HomeDisplayItem.SpecialProductsSliderItem>(binding) {

    private var currentPosition = eventListener
        .getCurrentSpecialProductItemPosition()

    private val handler = Handler(Looper.getMainLooper())
    private val specialProductSliderAdapter = SpecialProductSliderAdapter()
    val slider = binding.specialProductSlider.also {
        it.adapter = specialProductSliderAdapter
    }

    private val autoSlider = AutoSliderRunnable()

    init {
        binding.dotsIndicator.attachTo(slider)
        handler.post(autoSlider)
        itemView.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                Timber.d("Action down")
                handler.removeCallbacksAndMessages(null)
            } else if (event.action == MotionEvent.ACTION_UP) {
                handler.post(autoSlider)
            }
            v.performClick()
        }
    }

    override fun bind(item: HomeDisplayItem.SpecialProductsSliderItem) {
        specialProductSliderAdapter.submitList(item.specialProductImages)
    }


    inner class AutoSliderRunnable : Runnable {
        override fun run() {
            if (currentPosition <= 0) {
                currentPosition = reversePosition()
            } else {
                currentPosition--
            }
            eventListener.onSliderItemPositionChanged(currentPosition)
            slider.currentItem = currentPosition
            handler.postDelayed(this, nextItemDelay)
        }

        private fun reversePosition(): Int {
            val adapter = slider.adapter ?: return 0
            return adapter.itemCount - 1
        }
    }


    interface EventListener {
        fun onSliderItemPositionChanged(currentPosition: Int)
        fun getCurrentSpecialProductItemPosition(): Int
    }

    companion object {
        private const val nextItemDelay = 5000L
    }
}