package ir.jatlin.topmarket.ui.home.slider

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ir.jatlin.topmarket.core.network.model.common.NetworkImage
import ir.jatlin.topmarket.databinding.SpecialProductSlideViewBinding

class SpecialProductSliderAdapter: ListAdapter<NetworkImage,
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

    private class DifCallback : DiffUtil.ItemCallback<NetworkImage>() {

        override fun areItemsTheSame(
            oldItem: NetworkImage,
            newItem: NetworkImage
        ): Boolean {
            return newItem.id == oldItem.id
        }

        override fun areContentsTheSame(
            oldItem: NetworkImage,
            newItem: NetworkImage
        ): Boolean {
            return newItem == oldItem
        }
    }
}