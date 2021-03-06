package ir.jatlin.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import ir.jatlin.core.model.order.OrderStatus

@Entity(
    tableName = "orders",

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