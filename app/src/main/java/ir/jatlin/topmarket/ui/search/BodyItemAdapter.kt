package ir.jatlin.topmarket.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ir.jatlin.topmarket.databinding.SearchResultBodyItemViewBinding


class BodyItemAdapter(
    private val onProductInCategoryClicked: (categoryId: Int, productId: Int) -> Unit
) : ListAdapter<SearchDisplayItem.BodyItem, BodyItemViewHolder>(DiffCallback()),
    BodyItemViewHolder.EventListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BodyItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return BodyItemViewHolder(
            binding = SearchResultBodyItemViewBinding
                .inflate(inflater, parent, false),
            this
        )
    }

    override fun onBindViewHolder(holder: BodyItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<SearchDisplayItem.BodyItem>() {

        override fun areItemsTheSame(
            oldItem: SearchDisplayItem.BodyItem,
            newItem: SearchDisplayItem.BodyItem
        ): Boolean {
            return newItem.id == oldItem.id
        }

        override fun areContentsTheSame(
            oldItem: SearchDisplayItem.BodyItem,
            newItem: SearchDisplayItem.BodyItem
        ): Boolean {
            return newItem == oldItem
        }
    }

    override fun onProductInCategoryClick(categoryId: Int, productId: Int) {
        onProductInCategoryClicked(categoryId, productId)
    }
}