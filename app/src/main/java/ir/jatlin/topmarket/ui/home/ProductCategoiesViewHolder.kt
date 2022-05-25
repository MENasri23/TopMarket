package ir.jatlin.topmarket.ui.home

import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import ir.jatlin.topmarket.databinding.ProductCategoryViewBinding
import ir.jatlin.topmarket.ui.home.category.ProductCategoryAdapter
import ir.jatlin.topmarket.ui.listener.CategoryItemEventListener
import ir.jatlin.topmarket.ui.listener.ProductItemEventListener
import ir.jatlin.topmarket.ui.viewholder.BaseViewHolder


class ProductCategoryViewHolder(
    private val binding: ProductCategoryViewBinding,
    private val eventListener: CategoryItemEventListener,
    productItemEventListener: ProductItemEventListener
) : BaseViewHolder<ProductHomeItem.CategoriesItem>(binding), View.OnClickListener {

    private val context = binding.root.context
    private val adapter = ProductCategoryAdapter(productItemEventListener)

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

    override fun onClick(v: View?) {
        with(binding) {
            when (v?.id) {
                showMore.id -> eventListener.onShowMoreClick()
            }
        }
    }
}