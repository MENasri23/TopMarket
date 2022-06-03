package ir.jatlin.topmarket.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import ir.jatlin.topmarket.core.model.order.OrderStatus

@Entity(
    tableName = "orders",
    foreignKeys = [
        ForeignKey(
            entity = CustomerEntity::class,
            parentColumns = ["id"],
            childColumns = ["customer_id"]
        )
    ]
)
data class OrderEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo(name = "customer_id")
    val customerId: Int,
    val status: OrderStatus
)