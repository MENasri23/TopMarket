package ir.jatlin.topmarket.ui.product

import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import ir.jatlin.topmarket.databinding.ProductDisplayGroupViewBinding
import ir.jatlin.topmarket.ui.home.HomeDisplayItem
import ir.jatlin.topmarket.ui.viewholder.BaseViewHolder

private typealias ProductDisplayGroupItem = HomeDisplayItem.ProductDisplayGroupItem

class ProductDisplayGroupViewHolder(
    private val binding: ProductDisplayGroupViewBinding,
    private val itemEventListener: ProductDisplayGroupEventListener,
    productItemEventListener: ProductItemEventListener
) : BaseViewHolder<ProductDisplayGroupItem>(binding), View.OnClickListener {

    private val context = binding.root.context
    private val adapter = ProductDisplayAdapter(productItemEventListener)

    init {
        binding.apply {
            products.adapter = adapter
            val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL)
            products.addItemDecoration(itemDecoration)
        }
    }

    override fun bind(item: ProductDisplayGroupItem) {
        binding.tvCategoryLabel.text = context.getString(item.label)
        val displayItem = item.products
        adapter.submitList(displayItem)
    }

    override fun onClick(v: View?) {
        with(binding) {
            when (v?.id) {
                showMore.id -> itemEventListener.onShowMoreClick()
            }
        }
    }
}