package ir.jatlin.topmarket.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ir.jatlin.topmarket.core.model.product.PurchaseProduct

@Entity(
    tableName = "purchase_products"
)
data class PurchaseProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    @ColumnInfo(name = "product_id")
    val productId: Int,
    @ColumnInfo(name = "product_name")
    val productName: String,
    @ColumnInfo(name = "product_price")
    val productPrice: String,
    val count: Int,
)

fun PurchaseProductEntity.asPurchaseProduct() = PurchaseProduct(
    id = id,
    productId = productId,
    productName = productName,
    productPrice = productPrice,
    count = count
)