package ir.jatlin.topmarket.core.database.dao

import androidx.room.*
import ir.jatlin.topmarket.core.database.entity.PurchaseProductEntity

@Dao
interface PurchaseProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(purchaseProducts: List<PurchaseProductEntity>)

    @Update
    suspend fun update(purchaseProducts: List<PurchaseProductEntity>)

    @Delete
    suspend fun delete(purchaseProducts: List<PurchaseProductEntity>)

}