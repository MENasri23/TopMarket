package ir.jatlin.topmarket.ui.productdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ir.jatlin.topmarket.databinding.ProductReviewItemViewBinding

class ProductReviewAdapter(
    private val onEachProductReviewCLick: () -> Unit
) : ListAdapter<ProductReviewItem, ProductReviewItemViewHolder>(DiffCallback()),
    ProductReviewItemViewHolder.EventListener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductReviewItemViewHolder {
        return ProductReviewItemViewHolder(
            binding = ProductReviewItemViewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            eventListener = this
        )
    }

    override fun onBindViewHolder(holder: ProductReviewItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onPreviewClick() {
        onEachProductReviewCLick()
    }

    private class DiffCallback : DiffUtil.ItemCallback<ProductReviewItem>() {
        override fun areItemsTheSame(
            oldItem: ProductReviewItem,
            newItem: ProductReviewItem
        ): Boolean {
            return newItem.id == oldItem.id
        }

        override fun areContentsTheSame(
            oldItem: ProductReviewItem,
            newItem: ProductReviewItem
        ): Boolean {
            return newItem == oldItem
        }
    }
}
