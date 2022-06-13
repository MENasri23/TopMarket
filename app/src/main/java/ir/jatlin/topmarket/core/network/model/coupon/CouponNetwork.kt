package ir.jatlin.topmarket.core.network.model.coupon

import com.google.gson.annotations.SerializedName

data class CouponNetwork(
    val id: Int,
    val code: String,
    val amount: String,
    @SerializedName("date_expires_gmt")
    val expireDateGmt: String?
)

