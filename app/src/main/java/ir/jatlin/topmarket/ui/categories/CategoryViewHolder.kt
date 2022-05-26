package ir.jatlin.topmarket.ui.categories

import ir.jatlin.topmarket.databinding.CategoryItemViewBinding
import ir.jatlin.topmarket.ui.viewholder.BaseViewHolder

class CategoryViewHolder(
    private val binding: CategoryItemViewBinding,
    private val eventListener: CategoryDisplayItemEventListener
) : BaseViewHolder<ProductCategoryDisplayItem.CategoryItem>(binding) {


    override fun bind(item: ProductCategoryDisplayItem.CategoryItem) {
        // TODO: bind views
    }
}