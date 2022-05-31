package ir.jatlin.topmarket.ui.product.preview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ir.jatlin.topmarket.core.network.model.product.NetworkProduct
import ir.jatlin.topmarket.databinding.ProductPreviewItemViewBinding
import ir.jatlin.topmarket.ui.product.ProductItemEventListener
import ir.jatlin.topmarket.ui.viewholder.BaseViewHolder

class ProductPreviewAdapter(
    private val onProductClicked: (productId: Int) -> Unit
) : ListAdapter<NetworkProduct, BaseViewHolder<NetworkProduct>>(DiffCallback()),
    ProductItemEventListener {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<NetworkProduct> {
        return ProductPreviewItemViewHolder(
            binding = ProductPreviewItemViewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            eventListener = this
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder<NetworkProduct>, position: Int) {
        holder.bind(getItem(position))
    }

    private class DiffCallback : DiffUtil.ItemCallback<NetworkProduct>() {
        override fun areItemsTheSame(oldItem: NetworkProduct, newItem: NetworkProduct): Boolean {
            return newItem.id == oldItem.id
        }

        override fun areContentsTheSame(oldItem: NetworkProduct, newItem: NetworkProduct): Boolean {
            return newItem == oldItem
        }
    }

    override fun onProductClick(productId: Int) {
        onProductClicked(productId)
    }
}