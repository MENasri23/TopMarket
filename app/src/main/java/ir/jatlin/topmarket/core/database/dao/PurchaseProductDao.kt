package ir.jatlin.topmarket.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import ir.jatlin.topmarket.core.database.entity.PurchaseProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PurchaseProductDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(product: PurchaseProductEntity): Int


    fun getAllPurchaseProducts(): Flow<List<PurchaseProductEntity>>
}