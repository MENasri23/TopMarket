package ir.jatlin.topmarket.ui.purchase.cart

import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.core.model.product.CartProduct
import ir.jatlin.topmarket.databinding.LayoutCartProductItemViewBinding
import ir.jatlin.topmarket.ui.util.gone
import ir.jatlin.topmarket.ui.util.loadFromUrl
import ir.jatlin.topmarket.ui.util.visible
import ir.jatlin.topmarket.ui.util.withSeparator
import ir.jatlin.topmarket.ui.viewholder.BaseViewHolder

class CartProductViewHolder(
    private val binding: LayoutCartProductItemViewBinding
) : BaseViewHolder<CartProduct>(binding) {

    private val context = binding.root.context

    override fun bind(item: CartProduct) {
        with(binding) {
            orderItemProductName.text = item.productName
            tvQuantity.text = item.quantity.toString()
            cartProductPrice.text = item.totalPrice.toString().withSeparator()

            val discount = item.regularPrice - item.totalPrice
            if (discount > 0) {
                cartProductDiscount.text =
                    context.getString(
                        R.string.cart_discount,
                        discount.toString().withSeparator()
                    )
                cartProductDiscount.visible()
            } else cartProductDiscount.gone()

            orderItemProductImage.loadFromUrl(item.url)
        }
    }
}