package ir.jatlin.topmarket.core.domain.signin

import ir.jatlin.core.shared.fail.ErrorHandler
import ir.jatlin.topmarket.core.data.di.IODispatcher
import ir.jatlin.topmarket.core.data.repository.customer.CustomerRepository
import ir.jatlin.topmarket.core.domain.CoroutineUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class RegisterCustomerByEmailUseCase @Inject constructor(
    private val customerRepository: CustomerRepository,
    errorHandler: ErrorHandler,
    @IODispatcher dispatcher: CoroutineDispatcher
) : CoroutineUseCase<String, Int>(errorHandler, dispatcher) {

    override suspend fun execute(params: String): Int {
        return customerRepository.createCustomerByEmail(email = params)
    }
}