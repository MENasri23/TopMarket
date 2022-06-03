package ir.jatlin.topmarket.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ir.jatlin.topmarket.core.model.order.OrderLineItem

@Entity(
    tableName = "order_items",
    foreignKeys = [
        ForeignKey(
            entity = OrderEntity::class,
            parentColumns = ["id"],
            childColumns = ["order_id"]
        )
    ]
)
data class OrderLineItemEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "order_id")
    val orderId: Int,
    @ColumnInfo(name = "product_id")
    val productId: Int,
    @ColumnInfo(name = "product_name")
    val productName: String,
    @ColumnInfo(name = "total_price")
    val totalPrice: String,
    val quantity: Int,
)

fun OrderLineItemEntity.asOrderLineItem() = OrderLineItem(
    id = id,
    productId = productId,
    productName = productName,
    totalPrice = totalPrice,
    quantity = quantity
)