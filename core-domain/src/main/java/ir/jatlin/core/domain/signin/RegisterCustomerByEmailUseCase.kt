package ir.jatlin.core.domain.signin

import ir.jatlin.core.data.di.IODispatcher
import ir.jatlin.core.data.repository.customer.CustomerRepository
import ir.jatlin.core.domain.CoroutineUseCase
import ir.jatlin.core.shared.fail.ErrorHandler
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