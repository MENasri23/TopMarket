package ir.jatlin.topmarket.ui.home.slider

import ir.jatlin.topmarket.core.network.model.common.NetworkImage
import ir.jatlin.topmarket.databinding.SpecialProductSlideViewBinding
import ir.jatlin.topmarket.ui.home.HomeDisplayItem
import ir.jatlin.topmarket.ui.util.loadFromUrl
import ir.jatlin.topmarket.ui.viewholder.BaseViewHolder

class SpecialProductSliderViewHolder(
    private val binding: SpecialProductSlideViewBinding
) : BaseViewHolder<NetworkImage>(binding) {

    override fun bind(item: NetworkImage) {
        binding.specialImage.loadFromUrl(item.url)
    }


}