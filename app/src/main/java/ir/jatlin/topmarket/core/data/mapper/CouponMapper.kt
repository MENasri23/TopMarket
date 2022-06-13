package ir.jatlin.topmarket.core.data.mapper

import ir.jatlin.topmarket.core.model.coupon.Coupon
import ir.jatlin.topmarket.core.network.model.coupon.CouponNetwork

fun CouponNetwork.asCoupon() = Coupon(
    id = id,
    code = code,
    amount = amount,
    expireDateGmt = expireDateGmt
)