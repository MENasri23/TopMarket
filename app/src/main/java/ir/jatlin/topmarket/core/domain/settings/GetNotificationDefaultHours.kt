package ir.jatlin.topmarket.core.domain.settings

import ir.jatlin.core.data.di.IODispatcher
import ir.jatlin.core.shared.fail.ErrorHandler
import ir.jatlin.topmarket.core.domain.CoroutineUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetPreSetNotificationIntervals @Inject constructor(
    errorHandler: ErrorHandler,
    @IODispatcher dispatcher: CoroutineDispatcher
) : CoroutineUseCase<Unit, List<Int>>(errorHandler, dispatcher) {

    override suspend fun execute(params: Unit): List<Int> {
        return listOf(3, 5, 8, 12)
    }
}