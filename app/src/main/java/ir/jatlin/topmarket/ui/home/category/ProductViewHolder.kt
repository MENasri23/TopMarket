package ir.jatlin.topmarket.ui.home.category

import android.graphics.Paint
import androidx.core.view.isVisible
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.core.network.model.product.NetworkProduct
import ir.jatlin.topmarket.databinding.ProductCategoryItemViewBinding
import ir.jatlin.topmarket.ui.util.*
import ir.jatlin.topmarket.ui.viewholder.BaseViewHolder


class ProductViewHolder(
    private val binding: ProductCategoryItemViewBinding
) : BaseViewHolder<CategoryItem.ProductItem>(binding) {

    private var currentProduct: NetworkProduct? = null
    private val context = binding.root.context

    override fun bind(item: CategoryItem.ProductItem) {
        val product = item.data.also { currentProduct = it }
        with(binding) {
            showPrice(product.regularPrice, product.price)
            productName.text = product.name
            productStockStatus.text = if (product.stockStatus == IN_STOCK) {
                context.resources.getString(R.string.product_avaialble)
            } else {
                context.resources.getString(R.string.product_unavailable)
            }

            productImage.loadFromUrl(product.images.first().url)

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

    companion object {
        private const val IN_STOCK = "instock"
    }


}