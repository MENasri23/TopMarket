package ir.jatlin.topmarket.ui.purchase.cart

import ir.jatlin.topmarket.core.model.product.CartProduct
import ir.jatlin.topmarket.databinding.LayoutCartProductItemViewBinding
import ir.jatlin.topmarket.ui.util.loadFromUrl
import ir.jatlin.topmarket.ui.viewholder.BaseViewHolder

class CartProductViewHolder(
    private val binding: LayoutCartProductItemViewBinding
) : BaseViewHolder<CartProduct>(binding) {

    override fun bind(item: CartProduct) {
        binding.orderItemProductName.text = item.productName
        binding.orderItemProductImage.loadFromUrl(item.url)
        binding.tvQuantity.text = item.quantity.toString()
        binding.cartProductPrice.text = item.totalPrice
    }
}