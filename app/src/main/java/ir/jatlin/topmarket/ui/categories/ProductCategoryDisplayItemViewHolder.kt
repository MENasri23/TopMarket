package ir.jatlin.topmarket.ui.categories

import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.databinding.ProductCategoryItemViewBinding
import ir.jatlin.topmarket.ui.util.loadFromUrl
import ir.jatlin.topmarket.ui.viewholder.BaseViewHolder

class ProductCategoryDisplayItemViewHolder(
    private val binding: ProductCategoryItemViewBinding,
    private val eventListener: ProductCategoryDisplayItemEventListener
) : BaseViewHolder<ProductCategoryDisplayItem.CategoryItem>(binding) {

    private var currentCategoryId: Int? = null

    init {
        binding.categoryContainer.setOnClickListener {
            val id = currentCategoryId ?: return@setOnClickListener
            eventListener.onCategoryItemClick(id)
        }
    }

    override fun bind(item: ProductCategoryDisplayItem.CategoryItem) {
        val category = item.data
        currentCategoryId = category.id
        with(binding) {
            categoryName.text = category.name
            productCount.text =
                root.context.getString(R.string.available_product_count, category.productCount)
            categoryImage.loadFromUrl(category.image?.url)
        }
    }
}


interface ProductCategoryDisplayItemEventListener {
    fun onCategoryItemClick(categoryId: Int)
}