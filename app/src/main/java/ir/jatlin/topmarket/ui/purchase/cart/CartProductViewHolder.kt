package ir.jatlin.topmarket.ui.purchase.cart

import android.view.View
import ir.jatlin.core.model.product.CartProduct
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.databinding.LayoutCartProductItemViewBinding
import ir.jatlin.topmarket.ui.util.*
import ir.jatlin.topmarket.ui.viewholder.BaseViewHolder

class CartProductViewHolder(
    private val binding: LayoutCartProductItemViewBinding,
    private val eventListener: EventListener
) : BaseViewHolder<CartProduct>(binding), View.OnClickListener {

    private val context = binding.root.context
    private var cartProduct: CartProduct? = null

    init {
        binding.ivAdd.setOnClickListener(this)
        binding.ivRemove.setOnClickListener(this)
        binding.orderItemProductImage.setOnClickListener(this)
    }

    override fun bind(item: CartProduct) {
        cartProduct = item
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

            ivRemove.setImageResource(
                if (item.quantity > 1) R.drawable.ic_minus else R.drawable.ic_delete
            )
            orderItemProductImage.loadFromUrl(item.url)
            stopAnimation()
        }
    }

    override fun onClick(v: View?) {
        val product = cartProduct ?: return
        with(binding) {
            when (v?.id) {
                ivAdd.id -> eventListener.onAddToOrderClick(product, adapterPosition)
                ivRemove.id -> eventListener.onRemoveFromOrderClick(product, adapterPosition)
                orderItemProductImage.id -> eventListener.onCartProductClick(product.productId)
            }
        }
    }

    fun playAnimation() {
        with(binding) {
            if (orderQuantityLoading.isAnimating) return@with
            tvQuantity.invisible()
            orderQuantityLoadingContainer.visible()
            orderQuantityLoading.playAnimation()
        }
    }

    fun stopAnimation() {
        with(binding) {
            tvQuantity.visible()
            orderQuantityLoading.pauseAnimation()
            orderQuantityLoadingContainer.gone()
        }
    }

    interface EventListener {
        fun onAddToOrderClick(cartProduct: CartProduct, position: Int)
        fun onRemoveFromOrderClick(cartProduct: CartProduct, position: Int)
        fun onCartProductClick(productId: Int)
    }
}