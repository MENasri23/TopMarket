package ir.jatlin.topmarket.ui.home

import ir.jatlin.topmarket.databinding.ProductCategoryViewBinding
import ir.jatlin.topmarket.ui.home.category.ProductCategoryAdapter
import ir.jatlin.topmarket.ui.home.category.ProductCategoryItem
import ir.jatlin.topmarket.ui.viewholder.BaseViewHolder


class ProductCategoriesViewHolder(
    private val binding: ProductCategoryViewBinding
) : BaseViewHolder<ProductCategoriesItem.CategoryItem>(binding) {

    private val context = binding.root.context
    private val adapter = ProductCategoryAdapter()

    init {
        binding.apply {
            productCategories.adapter = adapter
        }
    }

    override fun bind(item: ProductCategoriesItem.CategoryItem) {
        binding.tvCategoryLabel.text = item.label
        val products = item.data
        adapter.submitList(products)
    }

}