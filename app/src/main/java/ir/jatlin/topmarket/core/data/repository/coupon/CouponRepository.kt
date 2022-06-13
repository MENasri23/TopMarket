package ir.jatlin.topmarket.core.data.repository.coupon

import ir.jatlin.topmarket.core.data.mapper.asCoupon
import ir.jatlin.topmarket.core.data.source.remote.coupon.CouponRemoteDataSource
import ir.jatlin.topmarket.core.model.coupon.Coupon
import javax.inject.Inject

class CouponRepository @Inject constructor(
    private val remoteDataSource: CouponRemoteDataSource
) {

    suspend fun getCouponByCode(code: String): Coupon? {
        return remoteDataSource.getCouponByCode(code).firstOrNull()?.asCoupon()
    }

}