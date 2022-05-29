package ir.jatlin.topmarket.ui.search

import ir.jatlin.topmarket.databinding.SearchSuggestProductItemViewBinding
import ir.jatlin.topmarket.ui.util.loadFromUrl
import ir.jatlin.topmarket.ui.viewholder.BaseViewHolder

class HeaderItemViewHolder(
    private val binding: SearchSuggestProductItemViewBinding,
    private val eventListener: EventListener
) : BaseViewHolder<SearchProductItem>(binding) {


    override fun bind(item: SearchProductItem) {
        with(binding) {
            productName.text = item.name
            productImage.loadFromUrl(item.imageUrl)
        }
    }

    interface EventListener {
        fun onClick(productId: Int)
    }
}