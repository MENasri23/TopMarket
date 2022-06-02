package ir.jatlin.topmarket.core.database.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import ir.jatlin.topmarket.core.model.user.CustomerPurchaseProducts

data class CustomerWithPurchaseProducts(
    @Embedded
    val customer: CustomerEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = CustomerPurchaseProductCrossRef::class,
            parentColumn = "customer_id",
            entityColumn = "purchase_product_id"
        )
    )
    val purchaseProducts: List<PurchaseProductEntity>

)

fun CustomerWithPurchaseProducts.asCustomerPurchaseProducts() = CustomerPurchaseProducts(
    customer = customer.asCustomer(),
    purchaseProducts = purchaseProducts.map(PurchaseProductEntity::asPurchaseProduct)
)

