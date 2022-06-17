package ir.jatlin.core.domain.signin

import ir.jatlin.core.data.di.IODispatcher
import ir.jatlin.core.data.repository.customer.CustomerRepository
import ir.jatlin.core.domain.FlowUseCase
import ir.jatlin.core.model.user.Customer
import ir.jatlin.core.shared.fail.ErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentCustomerStreamUseCase @Inject constructor(
    private val repository: CustomerRepository,
    errorHandler: ErrorHandler,
    @IODispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Int, Customer?>(errorHandler, dispatcher) {

    override fun execute(params: Int): Flow<Customer?> {
        return repository.findCustomerByIdStream(params)
    }
}