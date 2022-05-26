package ir.jatlin.topmarket.ui.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.databinding.CategoryDisplayGroupViewBinding
import ir.jatlin.topmarket.ui.viewholder.BaseViewHolder
import ir.jatlin.topmarket.ui.viewholder.ViewHolderCreator

private typealias CategoryDisplayViewHolder = BaseViewHolder<CategoryDisplayItem>

class CategoryDisplayItemAdapter(
    private val productCategoryGroupEventListener: ProductCategoryDisplayGroupItemEventListener,
    private val productCategoryItemEventListener: ProductCategoryDisplayItemEventListener
) :
    ListAdapter<CategoryDisplayItem, CategoryDisplayViewHolder>(
        CategoryDisplayItemDiffCallback()
    ),
    ViewHolderCreator<CategoryDisplayItem> {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryDisplayViewHolder {
        return createFrom(parent, viewType)
    }

    override fun onBindViewHolder(holder: CategoryDisplayViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is CategoryDisplayItem.ProductCategoryGroupItem ->
                R.layout.category_display_group_view
            else -> super.getItemViewType(position)
        }
    }


    @Suppress("UNCHECKED_CAST")
    override fun createFrom(
        parent: ViewGroup,
        viewType: Int
    ): CategoryDisplayViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val viewHolder: BaseViewHolder<*> = when (viewType) {
            R.layout.category_display_group_view ->
                ProductCategoryDisplayGroupItemViewHolder(
                    CategoryDisplayGroupViewBinding.inflate(inflater, parent, false),
                    productCategoryGroupEventListener,
                    productCategoryItemEventListener
                )
            else -> throw IllegalArgumentException("View type not found with identifier: $viewType")
        }

        return (viewHolder as? CategoryDisplayViewHolder)
            ?: throw AssertionError("$viewHolder can't be cast to BaseViewHolder")
    }

    class CategoryDisplayItemDiffCallback : DiffUtil.ItemCallback<CategoryDisplayItem>() {
        override fun areItemsTheSame(
            oldItem: CategoryDisplayItem,
            newItem: CategoryDisplayItem
        ): Boolean {
            return newItem.id == oldItem.id
        }

        override fun areContentsTheSame(
            oldItem: CategoryDisplayItem,
            newItem: CategoryDisplayItem
        ): Boolean {
            return newItem == oldItem
        }
    }
}