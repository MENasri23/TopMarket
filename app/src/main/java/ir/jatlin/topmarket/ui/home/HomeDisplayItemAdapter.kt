package ir.jatlin.topmarket.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.databinding.AmaziingSuggestionGroupViewBinding
import ir.jatlin.topmarket.databinding.ProductDisplayGroupViewBinding
import ir.jatlin.topmarket.databinding.SpecialProductSlideGroupBinding
import ir.jatlin.topmarket.databinding.SpecialProductSlideViewBinding
import ir.jatlin.topmarket.ui.home.amazinggroup.AmazingSuggestionGroupViewHolder
import ir.jatlin.topmarket.ui.home.slider.SpecialProductGroupViewHolder
import ir.jatlin.topmarket.ui.product.ProductDisplayGroupEventListener
import ir.jatlin.topmarket.ui.product.ProductDisplayGroupViewHolder
import ir.jatlin.topmarket.ui.product.ProductItemEventListener
import ir.jatlin.topmarket.ui.viewholder.BaseViewHolder
import ir.jatlin.topmarket.ui.viewholder.ViewHolderCreator

typealias ViewHolder = BaseViewHolder<HomeDisplayItem>

class HomeDisplayItemAdapter(
    private val displayItemEventListener: ProductDisplayGroupEventListener,
    private val productItemEventListener: ProductItemEventListener
) :
    ListAdapter<HomeDisplayItem, ViewHolder>(HomeDisplayItemDiffCallback()),
    ViewHolderCreator<HomeDisplayItem> {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return createFrom(parent, viewType)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is HomeDisplayItem.ProductDisplayGroupItem -> R.layout.product_display_group_view
            is HomeDisplayItem.AmazingSuggestionGroupItem -> R.layout.amaziing_suggestion_group_view
            is HomeDisplayItem.SpecialProductsSliderItem -> R.layout.special_product_slide_group
            else -> super.getItemViewType(position)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun createFrom(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val viewHolder: BaseViewHolder<*> = when (viewType) {
            R.layout.product_display_group_view ->
                ProductDisplayGroupViewHolder(
                    ProductDisplayGroupViewBinding.inflate(inflater, parent, false),
                    displayItemEventListener,
                    productItemEventListener
                )
            R.layout.amaziing_suggestion_group_view ->
                AmazingSuggestionGroupViewHolder(
                    binding = AmaziingSuggestionGroupViewBinding.inflate(inflater, parent, false)
                )
            R.layout.special_product_slide_group -> SpecialProductGroupViewHolder(
                binding = SpecialProductSlideGroupBinding.inflate(inflater, parent, false)
            )
            else -> throw IllegalArgumentException("View type not found with identifier: $viewType")
        }

        return (viewHolder as? ViewHolder)
            ?: throw AssertionError("$viewHolder can't be cast to BaseViewHolder")
    }

}

class HomeDisplayItemDiffCallback : DiffUtil.ItemCallback<HomeDisplayItem>() {

    override fun areItemsTheSame(
        oldItem: HomeDisplayItem,
        newItem: HomeDisplayItem
    ): Boolean {
        return newItem.id == oldItem.id
    }

    override fun areContentsTheSame(
        oldItem: HomeDisplayItem,
        newItem: HomeDisplayItem
    ): Boolean {
        return newItem == oldItem
    }
}
