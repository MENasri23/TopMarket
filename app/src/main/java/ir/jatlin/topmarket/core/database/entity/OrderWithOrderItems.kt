package ir.jatlin.topmarket.core.database.entity

import androidx.room.Embedded
import androidx.room.Relation
import ir.jatlin.topmarket.core.model.order.Order
import ir.jatlin.topmarket.core.model.user.Customer


data class OrderWithOrderItems(

    @Embedded
    val order: OrderEntity,

    @Relation(
        parentColumn = "customer_id",
        entityColumn = "id"
    )
    val customer: CustomerEntity?,

    @Relation(
        parentColumn = "id",
        entityColumn = "order_id"
    )
    val orderItems: List<OrderLineItemEntity>
)

fun OrderWithOrderItems.asOrder() = Order(
    id = order.id,
    customer = customer?.asCustomer() ?: Customer.Empty,
    orderItems = orderItems.map(OrderLineItemEntity::asOrderLineItem),
    status = order.status,
    totalPrice = order.totalPrice
)