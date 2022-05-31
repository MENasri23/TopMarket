package ir.jatlin.topmarket.ui.product.preview

import android.graphics.Paint
import androidx.core.view.isVisible
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.core.network.model.product.NetworkProduct
import ir.jatlin.topmarket.databinding.ProductPreviewItemViewBinding
import ir.jatlin.topmarket.ui.product.ProductItemEventListener
import ir.jatlin.topmarket.ui.util.*
import ir.jatlin.topmarket.ui.viewholder.BaseViewHolder

class ProductPreviewItemViewHolder(
    private val binding: ProductPreviewItemViewBinding,
    private val eventListener: ProductItemEventListener
) : BaseViewHolder<NetworkProduct>(binding) {

    private var currentProduct: NetworkProduct? = null
    private val context = binding.root.context

    init {
        binding.root.setOnClickListener {
            currentProduct?.let { product ->
                eventListener.onProductClick(product.id)
            }
        }
    }

    override fun bind(item: NetworkProduct) {
        currentProduct = item
        with(binding) {
            showPrice(item.regularPrice, item.price)
            productName.text = item.name
            productStockStatus.text = if (item.stockStatus == "instock") {
                context.resources.getString(R.string.product_avaialble)
            } else {
                context.resources.getString(R.string.product_unavailable)
            }

            productImage.loadFromUrl(item.images.first().url)

        }
    }

    private fun showPrice(regularPrice: String, price: String) = binding.apply {
        productPrice.text = withSeparator(price)
        productDiscount.setDiscount(
            beforeDiscount = regularPrice,
            afterDiscount = price
        )
        if (productDiscount.isVisible) {
            productDiscount.clipToRoundRect(true)

            productRegularPrice.text = withSeparator(regularPrice)
            productRegularPrice.visible()
            productRegularPrice.paintFlags =
                productRegularPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            productRegularPrice.invisible()
        }

    }

}