package ir.jatlin.core.data.mapper

import ir.jatlin.core.model.coupon.Coupon
import ir.jatlin.core.network.model.coupon.CouponNetwork

fun CouponNetwork.asCoupon() = Coupon(
    id = id,
    code = code,
    amount = amount,
    expireDateGmt = expireDateGmt
)