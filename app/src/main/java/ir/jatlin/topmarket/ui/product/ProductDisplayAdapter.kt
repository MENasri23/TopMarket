package ir.jatlin.topmarket.ui.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ir.jatlin.topmarket.databinding.ProductItemViewBinding
import ir.jatlin.topmarket.ui.viewholder.BaseViewHolder
import ir.jatlin.topmarket.ui.viewholder.ViewHolderCreator

/**
 * The product adapter for adapting a list of products.
 */

class ProductDisplayAdapter(
    private val productItemEventListener: ProductItemEventListener
) : ListAdapter<ProductDisplayItem, BaseViewHolder<ProductDisplayItem>>(
    ProductDisplayItemDiffCallback()
),
    ViewHolderCreator<ProductDisplayItem> {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ProductDisplayItem> {
        return createFrom(parent, viewType)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ProductDisplayItem>, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ProductDisplayItem.ProductItem -> ITEM_VIEW_TYPE_PRODUCT
            else -> super.getItemViewType(position)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun createFrom(parent: ViewGroup, viewType: Int): BaseViewHolder<ProductDisplayItem> {
        val inflater = LayoutInflater.from(parent.context)

        val viewHolder: BaseViewHolder<*> = when (viewType) {
            ITEM_VIEW_TYPE_PRODUCT ->
                ProductViewHolder(
                    ProductItemViewBinding.inflate(inflater, parent, false),
                    productItemEventListener
                )
            else -> throw IllegalArgumentException("View type not found with identifier: $viewType")
        }

        return (viewHolder as? BaseViewHolder<ProductDisplayItem>)
            ?: throw AssertionError("$viewHolder can't be cast to BaseViewHolder")
    }

    companion object {
        const val ITEM_VIEW_TYPE_PRODUCT = 0
    }

}

private class ProductDisplayItemDiffCallback : DiffUtil.ItemCallback<ProductDisplayItem>() {

    override fun areItemsTheSame(
        oldItem: ProductDisplayItem,
        newItem: ProductDisplayItem
    ): Boolean {
        return newItem.id == oldItem.id
    }

    override fun areContentsTheSame(
        oldItem: ProductDisplayItem,
        newItem: ProductDisplayItem
    ): Boolean {
        return newItem == oldItem
    }
}
