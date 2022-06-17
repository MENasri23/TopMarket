package ir.jatlin.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ir.jatlin.core.database.converter.OrderStatusConverter
import ir.jatlin.core.database.dao.CustomerDao
import ir.jatlin.core.database.dao.OrderDao
import ir.jatlin.core.database.dao.OrderItemDao
import ir.jatlin.core.database.entity.CustomerEntity
import ir.jatlin.core.database.entity.OrderEntity
import ir.jatlin.core.database.entity.OrderLineItemEntity

@Database(
    entities = [
        CustomerEntity::class,
        OrderEntity::class,
        OrderLineItemEntity::class
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(OrderStatusConverter::class)
abstract class MarketDatabase : RoomDatabase() {

    abstract fun customerDao(): CustomerDao
    abstract fun orderDao(): OrderDao
    abstract fun orderItemDao(): OrderItemDao
}