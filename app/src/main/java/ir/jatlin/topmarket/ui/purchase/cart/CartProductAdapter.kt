package ir.jatlin.topmarket.ui.purchase.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ir.jatlin.topmarket.core.model.product.CartProduct
import ir.jatlin.topmarket.databinding.LayoutCartProductItemViewBinding
import ir.jatlin.topmarket.ui.viewholder.BaseViewHolder

class CartProductAdapter(

) : ListAdapter<CartProduct, BaseViewHolder<CartProduct>>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<CartProduct> {
        return CartProductViewHolder(
            binding = LayoutCartProductItemViewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: BaseViewHolder<CartProduct>, position: Int) {
        holder.bind(getItem(position))
    }

    private class DiffCallback : DiffUtil.ItemCallback<CartProduct>() {

        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return newItem.orderLineId == oldItem.orderLineId
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return newItem == oldItem
        }
    }
}