package ir.jatlin.topmarket.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index


@Entity(
    tableName = "customers_purchase_products",
    primaryKeys = ["customer_id", "purchase_product_id"],
    foreignKeys = [
        ForeignKey(
            entity = CustomerEntity::class,
            parentColumns = ["id"],
            childColumns = ["customer_id"]
        ),
        ForeignKey(
            entity = PurchaseProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["purchase_product_id"]
        )
    ],
    indices = [
        Index(value = ["customer_id"]),
        Index(value = ["purchase_product_id"])
    ]
)
data class CustomerPurchaseProductCrossRef(
    @ColumnInfo(name = "customer_id")
    val customerId: Int,
    @ColumnInfo(name = "purchase_product_id")
    val purchaseProductId: Int
)