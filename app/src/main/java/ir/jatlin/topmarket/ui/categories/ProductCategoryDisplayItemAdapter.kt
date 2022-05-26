package ir.jatlin.topmarket.ui.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.databinding.CategoryDisplayGroupViewBinding
import ir.jatlin.topmarket.databinding.ProductCategoryItemViewBinding
import ir.jatlin.topmarket.ui.viewholder.BaseViewHolder
import ir.jatlin.topmarket.ui.viewholder.ViewHolderCreator

class ProductCategoryDisplayItemAdapter(
    private val productCategoryItemEventListener: ProductCategoryDisplayItemEventListener
) :
    ListAdapter<ProductCategoryDisplayItem, BaseViewHolder<ProductCategoryDisplayItem>>(
        ProductCategoryItemDiffCallback()
    ), ViewHolderCreator<ProductCategoryDisplayItem> {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ProductCategoryDisplayItem> {
        return createFrom(parent, viewType)
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<ProductCategoryDisplayItem>,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is ProductCategoryDisplayItem.CategoryItem -> R.layout.product_category_item_view
            else -> super.getItemViewType(position)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun createFrom(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ProductCategoryDisplayItem> {
        val inflater = LayoutInflater.from(parent.context)

        val viewHolder: BaseViewHolder<*> = when (viewType) {
            R.layout.product_category_item_view ->
                ProductCategoryDisplayItemViewHolder(
                    ProductCategoryItemViewBinding.inflate(inflater, parent, false),
                    productCategoryItemEventListener
                )
            else -> throw IllegalArgumentException("View type not found with identifier: $viewType")
        }

        return (viewHolder as? BaseViewHolder<ProductCategoryDisplayItem>)
            ?: throw AssertionError("$viewHolder can't be cast to BaseViewHolder")
    }

    class ProductCategoryItemDiffCallback : DiffUtil.ItemCallback<ProductCategoryDisplayItem>() {
        override fun areItemsTheSame(
            oldItem: ProductCategoryDisplayItem,
            newItem: ProductCategoryDisplayItem
        ): Boolean {
            return newItem.id == oldItem.id
        }

        override fun areContentsTheSame(
            oldItem: ProductCategoryDisplayItem,
            newItem: ProductCategoryDisplayItem
        ): Boolean {
            return newItem == oldItem
        }
    }


}