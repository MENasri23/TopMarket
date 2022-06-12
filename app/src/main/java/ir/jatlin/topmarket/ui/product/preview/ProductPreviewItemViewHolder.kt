package ir.jatlin.topmarket.ui.product.preview

import android.graphics.Paint
import androidx.core.view.isVisible
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.core.model.product.Product
import ir.jatlin.topmarket.databinding.ProductPreviewItemViewBinding
import ir.jatlin.topmarket.ui.product.ProductItemEventListener
import ir.jatlin.topmarket.ui.search.filter.SearchProductInCategoryItem
import ir.jatlin.topmarket.ui.util.*
import ir.jatlin.topmarket.ui.viewholder.BaseViewHolder

class ProductPreviewItemViewHolder(
    private val binding: ProductPreviewItemViewBinding,
    private val eventListener: ProductItemEventListener
) : BaseViewHolder<SearchProductInCategoryItem>(binding) {

    private var currentProduct: Product? = null
    private val context = binding.root.context

    init {
        binding.root.setOnClickListener {
            currentProduct?.let { product ->
                eventListener.onProductClick(product.id)
            }
        }
    }

    override fun bind(item: SearchProductInCategoryItem) {
        val product = item.product.also { currentProduct = it }
        with(binding) {
            showPrice(product.regularPrice, product.price)
            groupSpecial.isVisible = productDiscount.isVisible
            productName.text = product.name
            productStockStatus.text = if (product.stockStatus == "instock") {
                context.resources.getString(R.string.product_avaialble)
            } else {
                context.resources.getString(R.string.product_unavailable)
            }
            productAvgRating.text = product.averageRating
            productImage.loadFromUrl(product.images.firstOrNull()?.url)

        }
    }

    private fun showPrice(regularPrice: String, price: String) = binding.apply {
        if (price.isNotEmpty()) {
            productPrice.visible()
            productPrice.text = withSeparator(price)
        } else {
            productPrice.invisible()
        }
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
            productRegularPrice.gone()
        }

    }

}