package ir.jatlin.topmarket.ui.slider

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.databinding.ImageSliderItemViewBinding
import ir.jatlin.topmarket.ui.viewholder.BaseViewHolder
import ir.jatlin.topmarket.ui.viewholder.ViewHolderCreator

typealias SliderViewHolder = BaseViewHolder<SliderItem>

class SliderAdapter(

) : ListAdapter<SliderItem, SliderViewHolder>(SliderDiffCallback()),
    ViewHolderCreator<SliderItem> {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return createFrom(parent, viewType)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    @Suppress("UNCHECKED_CAST")
    override fun createFrom(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val inflater = layoutInflater(parent)
        val viewHolder: BaseViewHolder<*> = when (viewType) {
            R.layout.image_slider_item_view -> ImageSliderViewHolder(
                ImageSliderItemViewBinding.inflate(inflater, parent, false)
            )
            else -> throw IllegalArgumentException("View type not found with identifier: $viewType")
        }

        return (viewHolder as? SliderViewHolder)
            ?: throw AssertionError("$viewHolder can't be cast to the BaseViewHolder")
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is SliderItem.ImageItem -> R.layout.image_slider_item_view
            else -> super.getItemViewType(position)
        }
    }
}


class SliderDiffCallback : DiffUtil.ItemCallback<SliderItem>() {
    override fun areItemsTheSame(oldItem: SliderItem, newItem: SliderItem): Boolean {
        return newItem.id == oldItem.id
    }

    override fun areContentsTheSame(oldItem: SliderItem, newItem: SliderItem): Boolean {
        return newItem == oldItem
    }
}