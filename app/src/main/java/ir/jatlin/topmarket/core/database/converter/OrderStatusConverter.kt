package ir.jatlin.topmarket.core.database.converter

import androidx.room.TypeConverter
import ir.jatlin.core.model.order.OrderStatus
import ir.jatlin.core.model.order.asOrderStatusName

class OrderStatusConverter {

    @TypeConverter
    fun orderStatusToString(orderStatus: OrderStatus) =
        orderStatus.statusName

    @TypeConverter
    fun stringToOrderStatus(statusName: String) =
        statusName.asOrderStatusName()
}