package ir.jatlin.topmarket.core.domain.signin

import ir.jatlin.topmarket.core.data.di.IODispatcher
import ir.jatlin.topmarket.core.data.repository.customer.CustomerRepository
import ir.jatlin.topmarket.core.data.source.local.datastore.MarketPreferences
import ir.jatlin.topmarket.core.domain.CoroutineUseCase
import ir.jatlin.topmarket.core.model.user.Customer
import ir.jatlin.topmarket.core.shared.dataOnSuccessOr
import ir.jatlin.topmarket.core.shared.fail.ErrorHandler
import ir.jatlin.topmarket.core.shared.isSuccess
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
                val customerId = result.data!!
                marketPreferences.saveCustomerId(customerId)
                customerId
            } else null
        } else customer.id
    }
}