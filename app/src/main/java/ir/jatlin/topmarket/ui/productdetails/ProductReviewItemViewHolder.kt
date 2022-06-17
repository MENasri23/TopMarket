package ir.jatlin.topmarket.ui.productdetails

import ir.jatlin.topmarket.databinding.ProductReviewItemViewBinding
import ir.jatlin.topmarket.ui.util.setTextFromHtml
import ir.jatlin.topmarket.ui.viewholder.BaseViewHolder

class ProductReviewItemViewHolder(
    private val binding: ProductReviewItemViewBinding,
    eventListener: EventListener
) : BaseViewHolder<ProductReviewItem>(binding) {

    init {
        binding.productReviewItemContainer.setOnClickListener {
            eventListener.onPreviewClick()
        }
    }

    override fun bind(item: ProductReviewItem) {
        with(binding) {
            tvReviewerName.text = item.reviewerName
            tvReview.setTextFromHtml(item.content)
            tvCreatedDate.text = item.createdDate
        }
    }

    interface EventListener {
        fun onPreviewClick()
    }
}
