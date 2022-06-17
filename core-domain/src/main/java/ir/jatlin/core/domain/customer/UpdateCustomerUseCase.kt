package ir.jatlin.core.domain.customer

import ir.jatlin.core.data.repository.customer.CustomerRepository
import ir.jatlin.core.domain.CoroutineUseCase
import ir.jatlin.core.model.user.Customer
import ir.jatlin.core.shared.fail.ErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class UpdateCustomerUseCase @Inject constructor(
    private val customerRepository: CustomerRepository,
    errorHandler: ErrorHandler,
    dispatcher: CoroutineDispatcher
) : CoroutineUseCase<Customer, Customer>(errorHandler, dispatcher) {

    override suspend fun execute(params: Customer): Customer {
        return customerRepository.updateCustomer(params)
    }
}