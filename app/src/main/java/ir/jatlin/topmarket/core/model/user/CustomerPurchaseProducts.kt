package ir.jatlin.topmarket.core.model.user

import ir.jatlin.topmarket.core.model.product.PurchaseProduct

data class CustomerPurchaseProducts(
    val customer: Customer,
    val purchaseProducts: List<PurchaseProduct>
)