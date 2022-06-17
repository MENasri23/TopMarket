package ir.jatlin.topmarket.core.domain.coupon

import ir.jatlin.core.model.coupon.Coupon
import ir.jatlin.topmarket.core.data.di.IODispatcher
import ir.jatlin.topmarket.core.data.repository.coupon.CouponRepository
import ir.jatlin.topmarket.core.domain.CoroutineUseCase
import ir.jatlin.topmarket.core.shared.fail.ErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetCouponByCodeUseCase @Inject constructor(
    private val couponRepository: CouponRepository,
    errorHandler: ErrorHandler,
    @IODispatcher dispatcher: CoroutineDispatcher
) : CoroutineUseCase<String, Coupon?>(errorHandler, dispatcher) {

    override suspend fun execute(params: String): Coupon? {
        return couponRepository.getCouponByCode(code = params)
    }
}