package ir.jatlin.core.model.coupon

data class Coupon(
    val id: Int,
    val code: String,
    val amount: String,
    val expireDateGmt: String?
)