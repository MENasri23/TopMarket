package ir.jatlin.topmarket.ui.home.amazingitem

import android.graphics.Paint
import androidx.core.view.isVisible
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.core.network.model.product.NetworkProduct
import ir.jatlin.topmarket.databinding.AmazingSuggestionItemViewBinding
import ir.jatlin.topmarket.ui.util.*
import ir.jatlin.topmarket.ui.viewholder.BaseViewHolder

class AmazingSuggestionItemViewHolder(
    private val binding: AmazingSuggestionItemViewBinding
) : BaseViewHolder<AmazingDisplayItem.AmazingItem>(binding) {

    private var currentProduct: NetworkProduct? = null
    private val context = binding.root.context

    override fun bind(item: AmazingDisplayItem.AmazingItem) {
        val product = item.product.also { currentProduct = it }
        with(binding) {
            showPrice(product.regularPrice, product.price)
            productName.text = product.name
            productStockStatus.text = if (product.stockStatus == "instock") {
                context.resources.getString(R.string.product_avaialble)
            } else {
                context.resources.getString(R.string.product_unavailable)
            }

            productImage.loadFromUrl(product.images.first().url)

        }
    }

    private fun showPrice(regularPrice: String, price: String) = binding.apply {
        if (price.isNotBlank()) {
            productPrice.text = withSeparator(price)
        }
        productDiscount.setDiscount(
            beforeDiscount = regularPrice,
            afterDiscount = price
        )
        if (productDiscount.isVisible) {
            productDiscount.clipToRoundRect(true)

            productRegularPrice.paintFlags =
                productRegularPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            productPrice.invisible()
            productRegularPrice.invisible()
        }

    }


}