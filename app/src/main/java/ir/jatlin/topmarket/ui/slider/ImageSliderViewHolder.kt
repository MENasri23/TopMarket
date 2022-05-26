package ir.jatlin.topmarket.ui.slider

import ir.jatlin.topmarket.databinding.ImageSliderItemViewBinding
import ir.jatlin.topmarket.ui.util.loadFromUrl
import ir.jatlin.topmarket.ui.viewholder.BaseViewHolder

class ImageSliderViewHolder(
    private val binding: ImageSliderItemViewBinding
) : BaseViewHolder<SliderItem.ImageItem>(binding) {

    override fun bind(item: SliderItem.ImageItem) {
        binding.sliderImage.loadFromUrl(item.image.url)
    }
}