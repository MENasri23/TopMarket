package ir.jatlin.topmarket.core.domain.signin

import ir.jatlin.topmarket.core.data.di.IODispatcher
import ir.jatlin.topmarket.core.data.repository.customer.CustomerRepository
import ir.jatlin.topmarket.core.domain.CoroutineUseCase
import ir.jatlin.topmarket.core.model.user.Customer
import ir.jatlin.topmarket.core.shared.fail.ErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetCurrentCustomerUseCase @Inject constructor(
    private val repository: CustomerRepository,
    errorHandler: ErrorHandler,
    @IODispatcher dispatcher: CoroutineDispatcher
) : CoroutineUseCase<Int, Customer?>(errorHandler, dispatcher) {

    override suspend fun execute(params: Int): Customer? {
        return repository.findCustomerById(params)
    }
}