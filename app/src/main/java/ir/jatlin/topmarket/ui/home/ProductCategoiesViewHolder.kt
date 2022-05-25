package ir.jatlin.topmarket.ui.home

import androidx.recyclerview.widget.DividerItemDecoration
import ir.jatlin.topmarket.databinding.ProductCategoryViewBinding
import ir.jatlin.topmarket.ui.home.category.ProductCategoryAdapter
import ir.jatlin.topmarket.ui.listener.ProductCategoryEventListener
import ir.jatlin.topmarket.ui.viewholder.BaseViewHolder


class ProductCategoryViewHolder(
    private val binding: ProductCategoryViewBinding,
    eventListener: ProductCategoryEventListener
) : BaseViewHolder<ProductHomeItem.CategoriesItem>(binding) {

    private val context = binding.root.context
    private val adapter = ProductCategoryAdapter()

    init {
        binding.apply {
            products.adapter = adapter
            val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL)
            products.addItemDecoration(itemDecoration)
        }
    }

    override fun bind(item: ProductHomeItem.CategoriesItem) {
        binding.tvCategoryLabel.text = item.label
        val products = item.data
        adapter.submitList(products)
    }

}