package ir.jatlin.core.domain.settings

import ir.jatlin.core.data.di.IODispatcher
import ir.jatlin.core.data.source.local.datastore.MarketPreferences
import ir.jatlin.core.domain.FlowUseCase
import ir.jatlin.core.shared.fail.ErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotificationIntervalStreamUseCase @Inject constructor(
    private val marketPreferences: MarketPreferences,
    errorHandler: ErrorHandler,
    @IODispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, Int>(errorHandler, dispatcher) {

    override fun execute(params: Unit): Flow<Int> {
        return marketPreferences.notificationInterval
    }
}