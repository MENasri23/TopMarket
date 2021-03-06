package ir.jatlin.core.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ir.jatlin.core.model.user.Customer

@Entity(
    tableName = "customers"
)
data class CustomerEntity(
    @PrimaryKey
    val id: Int,
    val email: String,
    @ColumnInfo(defaultValue = "")
    val username: String,
    @ColumnInfo(name = "first_name", defaultValue = "")
    val firstName: String,
    @ColumnInfo(name = "last_name", defaultValue = "")
    val lastName: String,
    @ColumnInfo(name = "avatar_url", defaultValue = "")
    val avatarUrl: String,
    val role: String,
)

fun CustomerEntity.asCustomer() = Customer(
    id = id,
    email = email,
    username = username,
    firstName = firstName,
    lastName = lastName,
    avatarUrl = avatarUrl,
    role = role,
)

fun Customer.asCustomerEntity() = CustomerEntity(
    id = id,
    email = email,
    username = username,
    firstName = firstName,
    lastName = lastName,
    avatarUrl = avatarUrl,
    role = role,
)