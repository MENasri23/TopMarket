package ir.jatlin.topmarket.core.data.source.remote.coupon

import ir.jatlin.core.network.model.coupon.CouponNetwork

interface CouponRemoteDataSource {

    suspend fun getCouponByCode(code: String): List<CouponNetwork>

}