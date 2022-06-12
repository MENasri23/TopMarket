package ir.jatlin.topmarket.ui.home.slider

import ir.jatlin.topmarket.core.model.common.ProductImage
import ir.jatlin.topmarket.databinding.SpecialProductSlideViewBinding
import ir.jatlin.topmarket.ui.util.loadFromUrl
import ir.jatlin.topmarket.ui.viewholder.BaseViewHolder

class SpecialProductSliderViewHolder(
    private val binding: SpecialProductSlideViewBinding
) : BaseViewHolder<ProductImage>(binding) {

    override fun bind(item: ProductImage) {
        binding.specialImage.loadFromUrl(item.url)
    }


}