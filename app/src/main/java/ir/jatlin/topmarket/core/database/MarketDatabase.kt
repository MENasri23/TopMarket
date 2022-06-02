package ir.jatlin.topmarket.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.jatlin.topmarket.core.database.dao.CustomerDao
import ir.jatlin.topmarket.core.database.dao.PurchaseProductDao
import ir.jatlin.topmarket.core.database.entity.CustomerEntity
import ir.jatlin.topmarket.core.database.entity.CustomerPurchaseProductCrossRef
import ir.jatlin.topmarket.core.database.entity.PurchaseProductEntity

@Database(
    entities = [
        CustomerEntity::class,
        PurchaseProductEntity::class,
        CustomerPurchaseProductCrossRef::class
    ],
    version = 1,
    exportSchema = true
)
abstract class MarketDatabase : RoomDatabase() {

    abstract fun customerDao(): CustomerDao
    abstract fun purchaseProductDao(): PurchaseProductDao
}