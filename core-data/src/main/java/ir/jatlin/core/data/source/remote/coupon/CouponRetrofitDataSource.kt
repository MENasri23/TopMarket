package ir.jatlin.core.data.source.remote.coupon

import ir.jatlin.core.data.source.remote.ResponseConverter
import ir.jatlin.core.network.api.MarketApi
import ir.jatlin.core.network.model.coupon.CouponNetwork
import javax.inject.Inject

class CouponRetrofitDataSource @Inject constructor(
    private val marketApi: MarketApi,
    private val convertResponse: ResponseConverter
) : CouponRemoteDataSource {


    override suspend fun getCouponByCode(code: String): List<CouponNetwork> {
        return convertResponse(marketApi.getCoupon(code))
    }

}