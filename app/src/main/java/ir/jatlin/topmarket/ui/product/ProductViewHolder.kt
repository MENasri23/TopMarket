package ir.jatlin.topmarket.ui.product

import android.graphics.Paint
import android.view.View
import androidx.core.view.isVisible
import ir.jatlin.core.model.product.Product
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.databinding.ProductItemViewBinding
import ir.jatlin.topmarket.ui.util.*
import ir.jatlin.topmarket.ui.viewholder.BaseViewHolder

typealias ProductItem = ProductDisplayItem.ProductItem

class ProductViewHolder(
    private val binding: ProductItemViewBinding,
    private val eventListener: ProductItemEventListener
) : BaseViewHolder<ProductItem>(binding), View.OnClickListener {

    private var currentProduct: Product? = null
    private val context = binding.root.context

    init {
        binding.productContainer.setOnClickListener(this)
    }

    override fun bind(item: ProductItem) {
        val product = item.data.also { currentProduct = it }
        with(binding) {
            showPrice(product.regularPrice, product.price)
            productName.text = product.name
            productStockStatus.text = if (product.stockStatus == IN_STOCK) {
                context.resources.getString(R.string.product_avaialble)
            } else {
                context.resources.getString(R.string.product_unavailable)
            }

            productImage.loadFromUrl(product.images.firstOrNull()?.url)

        }
    }

    private fun showPrice(regularPrice: String, price: String) = binding.apply {
        productPrice.isVisible = regularPrice.isNotBlank()
        productPrice.text = price.withSeparator()
        productDiscount.setDiscount(
            beforeDiscount = regularPrice,
            afterDiscount = price
        )
        if (productDiscount.isVisible) {
            productDiscount.clipToRoundRect(true)

            productRegularPrice.text = regularPrice.withSeparator()
            productRegularPrice.visible()
            productRegularPrice.paintFlags =
                productRegularPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            productRegularPrice.invisible()
        }

    }

    companion object {
        private const val IN_STOCK = "instock"
    }

    override fun onClick(v: View?) {
        val product = currentProduct ?: return
        when (v?.id) {
            binding.productContainer.id -> eventListener.onProductClick(product.id)
        }
    }


}