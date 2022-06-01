package ir.jatlin.topmarket.ui.product.preview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ir.jatlin.topmarket.core.network.model.product.NetworkProduct
import ir.jatlin.topmarket.databinding.ProductPreviewItemViewBinding
import ir.jatlin.topmarket.ui.product.ProductItemEventListener
import ir.jatlin.topmarket.ui.search.filter.SearchProductInCategoryItem
import ir.jatlin.topmarket.ui.viewholder.BaseViewHolder

class ProductPreviewAdapter(
    private val onProductClicked: (productId: Int) -> Unit
) : ListAdapter<SearchProductInCategoryItem, BaseViewHolder<SearchProductInCategoryItem>>(
    DiffCallback()
),
    ProductItemEventListener {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<SearchProductInCategoryItem> {
        return ProductPreviewItemViewHolder(
            binding = ProductPreviewItemViewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            eventListener = this
        )
    }

    override fun onBindViewHolder(
        holder: BaseViewHolder<SearchProductInCategoryItem>,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    private class DiffCallback : DiffUtil.ItemCallback<SearchProductInCategoryItem>() {
        override fun areItemsTheSame(
            oldItem: SearchProductInCategoryItem,
            newItem: SearchProductInCategoryItem
        ): Boolean {
            return newItem.product.id == oldItem.product.id
        }

        override fun areContentsTheSame(
            oldItem: SearchProductInCategoryItem,
            newItem: SearchProductInCategoryItem
        ): Boolean {
            return newItem.product == oldItem.product
        }
    }

    override fun onProductClick(productId: Int) {
        onProductClicked(productId)
    }
}