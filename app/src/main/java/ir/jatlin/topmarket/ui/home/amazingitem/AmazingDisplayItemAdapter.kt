package ir.jatlin.topmarket.ui.home.amazingitem

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.databinding.AmazingSuggestionHeaderItemViewBinding
import ir.jatlin.topmarket.databinding.AmazingSuggestionItemViewBinding
import ir.jatlin.topmarket.ui.viewholder.BaseViewHolder

class AmazingDisplayItemAdapter(

) : ListAdapter<AmazingDisplayItem, BaseViewHolder<AmazingDisplayItem>>(

    AmazingDisplayItemDiffCallback()
) {

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<AmazingDisplayItem> {
        val inflater = LayoutInflater.from(parent.context)
        val viewHolder: BaseViewHolder<*> = when (viewType) {
            R.layout.amazing_suggestion_header_item_view -> {
                AmazingSuggestionHeaderViewHolder(
                    binding = AmazingSuggestionHeaderItemViewBinding
                        .inflate(inflater, parent, false)
                )
            }
            R.layout.amazing_suggestion_item_view -> {
                AmazingSuggestionItemViewHolder(
                    binding = AmazingSuggestionItemViewBinding
                        .inflate(inflater, parent, false)
                )
            }
            else -> throw IllegalArgumentException("Unknown view type")
        }

        return (viewHolder as? BaseViewHolder<AmazingDisplayItem>)
            ?: throw IllegalArgumentException("viewHolder $viewHolder can't be cast")
    }

    override fun onBindViewHolder(holder: BaseViewHolder<AmazingDisplayItem>, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is AmazingDisplayItem.AmazingItem -> R.layout.amazing_suggestion_item_view
            is AmazingDisplayItem.Header -> R.layout.amazing_suggestion_header_item_view
            else -> super.getItemViewType(position)
        }
    }
}

class AmazingDisplayItemDiffCallback : DiffUtil.ItemCallback<AmazingDisplayItem>() {
    override fun areItemsTheSame(
        oldItem: AmazingDisplayItem,
        newItem: AmazingDisplayItem
    ): Boolean {
        return newItem.id == oldItem.id
    }

    override fun areContentsTheSame(
        oldItem: AmazingDisplayItem,
        newItem: AmazingDisplayItem
    ): Boolean {
        return newItem == oldItem
    }
}
