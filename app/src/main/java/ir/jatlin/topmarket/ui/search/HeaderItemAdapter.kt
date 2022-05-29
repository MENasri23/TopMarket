package ir.jatlin.topmarket.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ir.jatlin.topmarket.databinding.SearchSuggestProductItemViewBinding


class HeaderItemAdapter(
    private val onProductItemClick: (productId: Int) -> Unit
) : ListAdapter<SearchProductItem, HeaderItemViewHolder>(DiffCallback()),
    HeaderItemViewHolder.EventListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeaderItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return HeaderItemViewHolder(
            binding = SearchSuggestProductItemViewBinding
                .inflate(inflater, parent, false),
            this
        )
    }

    override fun onBindViewHolder(holder: HeaderItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<SearchProductItem>() {
        override fun areItemsTheSame(
            oldItem: SearchProductItem,
            newItem: SearchProductItem
        ): Boolean {
            return newItem.id == oldItem.id
        }

        override fun areContentsTheSame(
            oldItem: SearchProductItem,
            newItem: SearchProductItem
        ): Boolean {
            return newItem == oldItem
        }
    }

    override fun onClick(productId: Int) {
        onProductItemClick(productId)
    }
}