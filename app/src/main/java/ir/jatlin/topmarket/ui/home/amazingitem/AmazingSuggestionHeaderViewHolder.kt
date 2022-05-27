package ir.jatlin.topmarket.ui.home.amazingitem

import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.databinding.AmazingSuggestionHeaderItemViewBinding
import ir.jatlin.topmarket.ui.viewholder.BaseViewHolder

class AmazingSuggestionHeaderViewHolder(
    private val binding: AmazingSuggestionHeaderItemViewBinding
) : BaseViewHolder<AmazingDisplayItem.Header>(binding) {

    init {
        binding.root
        binding.amazingShowMore.setOnClickListener {
            // TODO: Navigate to product list screen in future
        }
    }

    override fun bind(item: AmazingDisplayItem.Header) {
        val context = binding.root.context
        binding.amazingShowMore.text = if (item.label != 0) {
            context.getString(item.label)
        } else context.getString(R.string.show_all)
    }
}