package ir.jatlin.topmarket.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ir.jatlin.topmarket.core.database.converter.OrderStatusConverter
import ir.jatlin.topmarket.core.database.dao.CustomerDao
import ir.jatlin.topmarket.core.database.dao.OrderItemDao
import ir.jatlin.topmarket.core.database.entity.CustomerEntity
import ir.jatlin.topmarket.core.database.entity.CustomerOrderCrossRef
import ir.jatlin.topmarket.core.database.entity.OrderEntity
import ir.jatlin.topmarket.core.database.entity.OrderLineItemEntity

@Database(
    entities = [
        CustomerEntity::class,
        OrderEntity::class,
        OrderLineItemEntity::class,
        CustomerOrderCrossRef::class
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(OrderStatusConverter::class)
abstract class MarketDatabase : RoomDatabase() {

    abstract fun customerDao(): CustomerDao
    abstract fun orderItemDao(): OrderItemDao
}