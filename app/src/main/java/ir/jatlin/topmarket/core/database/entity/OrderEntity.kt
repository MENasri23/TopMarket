package ir.jatlin.topmarket.core.database.entity

import androidx.room.*
import ir.jatlin.topmarket.core.model.order.OrderStatus

@Entity(
    tableName = "orders",
)
data class OrderEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "customer_id")
    val customerId: Int,
    val status: OrderStatus
)