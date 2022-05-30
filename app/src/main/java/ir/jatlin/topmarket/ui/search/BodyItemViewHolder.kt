package ir.jatlin.topmarket.ui.search

import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.databinding.SearchResultBodyItemViewBinding
import ir.jatlin.topmarket.ui.util.setTextFromHtml
import ir.jatlin.topmarket.ui.viewholder.BaseViewHolder

class BodyItemViewHolder(
    private val binding: SearchResultBodyItemViewBinding,
    private val eventListener: EventListener
) : BaseViewHolder<SearchDisplayItem.BodyItem>(binding) {

    private var bodyItem: SearchDisplayItem.BodyItem? = null

    init {
        binding.root.setOnClickListener {
            bodyItem?.let {
                eventListener.onProductInCategoryClick(it.categoryId)
            }
        }
    }

    override fun bind(item: SearchDisplayItem.BodyItem) {
        bodyItem = item
        with(binding) {
            productName.text = item.productName
            categoryText.setTextFromHtml(
                root.context.getString(
                    R.string.product_in_category, item.categoryName
                )
            )
        }
    }

    interface EventListener {
        fun onProductInCategoryClick(categoryId: Int)
    }
}