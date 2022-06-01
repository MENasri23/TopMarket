package ir.jatlin.topmarket.ui.home.slider

import ir.jatlin.topmarket.databinding.SpecialProductSlideGroupBinding
import ir.jatlin.topmarket.ui.home.HomeDisplayItem
import ir.jatlin.topmarket.ui.viewholder.BaseViewHolder

class SpecialProductGroupViewHolder(
    binding: SpecialProductSlideGroupBinding
) : BaseViewHolder<HomeDisplayItem.SpecialProductsSliderItem>(binding) {

    private val specialProductSliderAdapter = SpecialProductSliderAdapter()

    init {
        with(binding) {
            specialProductSlider.adapter = specialProductSliderAdapter
            dotsIndicator.attachTo(specialProductSlider)
        }
    }

    override fun bind(item: HomeDisplayItem.SpecialProductsSliderItem) {
        specialProductSliderAdapter.submitList(item.specialProductImages)
    }
}