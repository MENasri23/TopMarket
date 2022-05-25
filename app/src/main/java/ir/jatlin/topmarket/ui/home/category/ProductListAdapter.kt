package ir.jatlin.topmarket.ui.home.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ir.jatlin.topmarket.databinding.ProductCategoryItemViewBinding
import ir.jatlin.topmarket.ui.listener.ProductItemEventListener
import ir.jatlin.topmarket.ui.viewholder.BaseViewHolder
import ir.jatlin.topmarket.ui.viewholder.ViewHolderCreator

typealias ViewHolder = BaseViewHolder<CategoryItem>

class ProductCategoryAdapter(
    private val productItemEventListener: ProductItemEventListener
) : ListAdapter<CategoryItem, ViewHolder>(ProductDiffCallback()),
    ViewHolderCreator<CategoryItem> {

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
            is CategoryItem.ProductItem -> ITEM_VIEW_TYPE_PRODUCT
            else -> super.getItemViewType(position)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun createFrom(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val viewHolder: BaseViewHolder<*> = when (viewType) {
            ITEM_VIEW_TYPE_PRODUCT ->
                ProductViewHolder(
                    ProductCategoryItemViewBinding.inflate(inflater, parent, false),
                    productItemEventListener
                )
            else -> throw IllegalArgumentException("View type not found with identifier: $viewType")
        }

        return (viewHolder as? ViewHolder)
            ?: throw AssertionError("$viewHolder can't be cast to BaseViewHolder")
    }

    companion object {
        const val ITEM_VIEW_TYPE_PRODUCT = 0
    }

}

class ProductDiffCallback : DiffUtil.ItemCallback<CategoryItem>() {

    override fun areItemsTheSame(
        oldItem: CategoryItem,
        newItem: CategoryItem
    ): Boolean {
        return newItem.id == oldItem.id
    }

    override fun areContentsTheSame(
        oldItem: CategoryItem,
        newItem: CategoryItem
    ): Boolean {
        return newItem == oldItem
    }
}
