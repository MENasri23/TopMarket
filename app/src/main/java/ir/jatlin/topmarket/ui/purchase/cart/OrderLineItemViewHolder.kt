package ir.jatlin.topmarket.ui.purchase.cart

import ir.jatlin.topmarket.core.model.order.OrderLineItem
import ir.jatlin.topmarket.databinding.LayoutOrderItemViewBinding
import ir.jatlin.topmarket.ui.viewholder.BaseViewHolder

class OrderLineItemViewHolder(
    private val binding: LayoutOrderItemViewBinding
) : BaseViewHolder<OrderLineItem>(binding) {

    override fun bind(item: OrderLineItem) {
        binding.orderItemProductName.text = item.productName
        binding.orderItemProductImage
    }
}