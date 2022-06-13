package ir.jatlin.topmarket.core.model.coupon

data class Coupon(
    val id: Int,
    val code: String,
    val amount: String,
    val expireDateGmt: String?
)