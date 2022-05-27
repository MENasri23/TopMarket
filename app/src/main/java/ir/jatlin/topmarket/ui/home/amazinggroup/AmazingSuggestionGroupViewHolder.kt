package ir.jatlin.topmarket.ui.home.amazinggroup

import ir.jatlin.topmarket.databinding.AmaziingSuggestionGroupViewBinding
import ir.jatlin.topmarket.ui.home.HomeDisplayItem
import ir.jatlin.topmarket.ui.home.amazingitem.AmazingDisplayItemAdapter
import ir.jatlin.topmarket.ui.viewholder.BaseViewHolder

class AmazingSuggestionGroupViewHolder(
    private val binding: AmaziingSuggestionGroupViewBinding,
) : BaseViewHolder<HomeDisplayItem.AmazingSuggestionGroupItem>(binding) {

    private val amazingDisplayItemAdapter = AmazingDisplayItemAdapter().also {
        binding.amazingSuggestions.adapter = it
    }

    override fun bind(item: HomeDisplayItem.AmazingSuggestionGroupItem) {
        binding.amazingGroupContainer.setBackgroundColor(
            (0xff000000 or item.backgroundColor).toInt()
        )
        amazingDisplayItemAdapter.submitList(item.suggestionItems)
    }
}