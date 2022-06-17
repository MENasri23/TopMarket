package ir.jatlin.topmarket.core.domain.signin

import ir.jatlin.core.shared.dataOnSuccessOr
import ir.jatlin.core.shared.fail.ErrorHandler
import ir.jatlin.core.shared.isSuccess
import ir.jatlin.topmarket.core.data.di.IODispatcher
import ir.jatlin.topmarket.core.data.source.local.datastore.MarketPreferences
import ir.jatlin.topmarket.core.domain.CoroutineUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class RegisterOrSignInCustomerUseCase @Inject constructor(
    private val marketPreferences: MarketPreferences,
    private val getRegisteredCustomerUseCase: GetRegisteredCustomerUseCase,
    private val registerCustomerByEmailUseCase: RegisterCustomerByEmailUseCase,
    errorHandler: ErrorHandler,
    @IODispatcher dispatcher: CoroutineDispatcher
) : CoroutineUseCase<String, Int?>(errorHandler, dispatcher) {

    override suspend fun execute(params: String): Int? {
        val customer = getRegisteredCustomerUseCase(params).dataOnSuccessOr(null)
        return if (customer == null) {
            /* Create new customer */
            val result = registerCustomerByEmailUseCase(params)
            if (result.isSuccess) {
                val customerId = result.data ?: return null
                marketPreferences.saveCustomerId(customerId)
                customerId
            } else null
        } else customer.id
    }
}