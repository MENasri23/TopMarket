package ir.jatlin.topmarket.core.database.converter

import androidx.room.TypeConverter
import ir.jatlin.topmarket.core.model.order.OrderStatus
import ir.jatlin.topmarket.core.model.order.asOrderStatusName

class OrderStatusConverter {

    @TypeConverter
    fun orderStatusToString(orderStatus: OrderStatus) =
        orderStatus.statusName

    @TypeConverter
    fun stringToOrderStatus(statusName: String) =
        statusName.asOrderStatusName()
}