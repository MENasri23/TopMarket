package ir.jatlin.topmarket.ui.categories

import ir.jatlin.topmarket.databinding.CategoryDisplayGroupViewBinding
import ir.jatlin.topmarket.ui.viewholder.BaseViewHolder

class ProductCategoryDisplayGroupItemViewHolder(
    private val binding: CategoryDisplayGroupViewBinding,
    private val eventListener: ProductCategoryDisplayGroupItemEventListener,
    productCategoryItemEventListener: ProductCategoryDisplayItemEventListener
) : BaseViewHolder<CategoryDisplayItem.ProductCategoryGroupItem>(binding) {

    private val context = binding.root.context
    private val categoryItemAdapter =
        ProductCategoryDisplayItemAdapter(productCategoryItemEventListener)

    init {
        with(binding) {
            categories.adapter = categoryItemAdapter
            showMore.setOnClickListener {
                eventListener.onShowMore()
            }
        }
    }

    override fun bind(item: CategoryDisplayItem.ProductCategoryGroupItem) {
        binding.tvCategoryLabel.text = item.label
        categoryItemAdapter.submitList(item.categories)
    }
}

interface ProductCategoryDisplayGroupItemEventListener {
    fun onShowMore()
}