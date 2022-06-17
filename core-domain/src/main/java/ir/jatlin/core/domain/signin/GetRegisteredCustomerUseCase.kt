package ir.jatlin.core.domain.signin

import ir.jatlin.core.data.di.IODispatcher
import ir.jatlin.core.data.repository.customer.CustomerRepository
import ir.jatlin.core.data.source.local.datastore.MarketPreferences
import ir.jatlin.core.domain.CoroutineUseCase
import ir.jatlin.core.model.user.Customer
import ir.jatlin.core.shared.fail.ErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class GetRegisteredCustomerUseCase @Inject constructor(
    private val repository: CustomerRepository,
    private val marketPreferences: MarketPreferences,
    errorHandler: ErrorHandler,
    @IODispatcher dispatcher: CoroutineDispatcher
) : CoroutineUseCase<String, Customer?>(errorHandler, dispatcher) {

    override suspend fun execute(params: String): Customer? {
        val customer = repository.findCustomerByEmail(email = params)
        if (customer != null) {
            val lastCustomerId = marketPreferences
                .purchasePreferencesStream.firstOrNull()?.customerId
            if (customer.id != lastCustomerId) {
                marketPreferences.saveCustomerId(customer.id)
            }
        }
        return customer
    }
}