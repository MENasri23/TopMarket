package ir.jatlin.topmarket.core.domain.signin

import ir.jatlin.topmarket.core.data.di.IODispatcher
import ir.jatlin.topmarket.core.data.repository.customer.CustomerRepository
import ir.jatlin.topmarket.core.data.source.local.datastore.MarketPreferences
import ir.jatlin.topmarket.core.domain.CoroutineUseCase
import ir.jatlin.topmarket.core.domain.FlowUseCase
import ir.jatlin.topmarket.core.model.purchase.PurchasePrefsInfo
import ir.jatlin.topmarket.core.model.user.Customer
import ir.jatlin.topmarket.core.shared.fail.ErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCurrentCustomerIdStreamUseCase @Inject constructor(
    private val marketPreferences: MarketPreferences,
    errorHandler: ErrorHandler,
    @IODispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, Int?>(errorHandler, dispatcher) {

    override fun execute(params: Unit): Flow<Int?> {
        return marketPreferences.purchasePreferencesStream.map {
            val customerId = it.customerId
            if (customerId != PurchasePrefsInfo.GUEST_CUSTOMER) {
                customerId
            } else null
        }

    }
}