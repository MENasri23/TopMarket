package ir.jatlin.topmarket.core.database.entity

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import ir.jatlin.topmarket.core.model.order.OrderStatus

@Entity(
    tableName = "orders",
    foreignKeys = [
        ForeignKey(
            entity = CustomerEntity::class,
            parentColumns = ["id"],
            childColumns = ["customer_id"],
            onDelete = CASCADE
        )
    ],
    indices = [
        Index(value = ["customer_id"])
    ]
)
data class OrderEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "customer_id")
    val customerId: Int,
    val status: OrderStatus,
    @ColumnInfo(name = "total_price")
    val totalPrice: String
)