package ir.jatlin.topmarket.ui.home.slider

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ir.jatlin.core.model.common.ProductImage
import ir.jatlin.topmarket.databinding.SpecialProductSlideViewBinding

class SpecialProductSliderAdapter : ListAdapter<ProductImage,
        SpecialProductSliderViewHolder>(DifCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SpecialProductSliderViewHolder {
        return SpecialProductSliderViewHolder(
            SpecialProductSlideViewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SpecialProductSliderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class DifCallback : DiffUtil.ItemCallback<ProductImage>() {

        override fun areItemsTheSame(
            oldItem: ProductImage,
            newItem: ProductImage
        ): Boolean {
            return newItem.id == oldItem.id
        }

        override fun areContentsTheSame(
            oldItem: ProductImage,
            newItem: ProductImage
        ): Boolean {
            return newItem == oldItem
        }
    }
}