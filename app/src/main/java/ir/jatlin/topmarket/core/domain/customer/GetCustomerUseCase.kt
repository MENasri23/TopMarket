package ir.jatlin.topmarket.core.domain.customer

import ir.jatlin.topmarket.core.data.repository.customer.CustomerRepository
import ir.jatlin.topmarket.core.domain.FlowUseCase
import ir.jatlin.topmarket.core.model.user.Customer
import ir.jatlin.topmarket.core.shared.fail.ErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCustomerUseCase @Inject constructor(
    private val customerRepository: CustomerRepository,
    errorHandler: ErrorHandler,
    dispatcher: CoroutineDispatcher
) : FlowUseCase<Int, Customer>(errorHandler, dispatcher) {

    override fun execute(params: Int): Flow<Customer> {
        return customerRepository.findCustomerById(params)
    }
}