package ir.jatlin.core.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CustomerWithOrders(
    @Embedded
    val customer: CustomerEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "customer_id",

    )
    val orders: List<OrderEntity>

)

