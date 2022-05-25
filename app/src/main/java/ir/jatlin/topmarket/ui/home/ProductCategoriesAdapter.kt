package ir.jatlin.topmarket.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ir.jatlin.topmarket.databinding.ProductCategoryViewBinding
import ir.jatlin.topmarket.ui.listener.ProductCategoryEventListener
import ir.jatlin.topmarket.ui.viewholder.BaseViewHolder
import ir.jatlin.topmarket.ui.viewholder.ViewHolderCreator

typealias ViewHolder = BaseViewHolder<ProductHomeItem>

class ProductCategoriesAdapter(
    private val eventListener: ProductCategoryEventListener
) :
    ListAdapter<ProductHomeItem, ViewHolder>(ProductDiffCallback()),
    ViewHolderCreator<ProductHomeItem> {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return createFrom(parent, viewType)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ProductHomeItem.CategoriesItem -> ITEM_VIEW_TYPE_PRODUCT_CATEGORY
            else -> super.getItemViewType(position)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun createFrom(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val viewHolder: BaseViewHolder<*> = when (viewType) {
            ITEM_VIEW_TYPE_PRODUCT_CATEGORY ->
                ProductCategoryViewHolder(
                    ProductCategoryViewBinding.inflate(inflater, parent, false),
                    eventListener
                )
            else -> throw IllegalArgumentException("View type not found with identifier: $viewType")
        }

        return (viewHolder as? ViewHolder)
            ?: throw AssertionError("$viewHolder can't be cast to BaseViewHolder")
    }

    companion object {
        const val ITEM_VIEW_TYPE_PRODUCT_CATEGORY = 0
    }

}

class ProductDiffCallback : DiffUtil.ItemCallback<ProductHomeItem>() {

    override fun areItemsTheSame(
        oldItem: ProductHomeItem,
        newItem: ProductHomeItem
    ): Boolean {
        return newItem.id == oldItem.id
    }

    override fun areContentsTheSame(
        oldItem: ProductHomeItem,
        newItem: ProductHomeItem
    ): Boolean {
        return newItem == oldItem
    }
}
