package ir.jatlin.topmarket.ui.search

import ir.jatlin.topmarket.databinding.SearchSuggestProductItemViewBinding
import ir.jatlin.topmarket.ui.util.loadFromUrl
import ir.jatlin.topmarket.ui.viewholder.BaseViewHolder

class SearchSuggestProductItemView(
    private val binding: SearchSuggestProductItemViewBinding
) : BaseViewHolder<SearchProductItem>(binding) {


    override fun bind(item: SearchProductItem) {
        with(binding) {
            binding.productName.text = item.name
            binding.productImage.loadFromUrl(item.imageUrl)
        }
    }
}