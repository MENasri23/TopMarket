package ir.jatlin.core.domain.settings

import ir.jatlin.core.data.di.IODispatcher
import ir.jatlin.core.data.source.local.datastore.MarketPreferences
import ir.jatlin.core.domain.CoroutineUseCase
import ir.jatlin.core.shared.fail.ErrorHandler
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class EnableNotificationUseCase @Inject constructor(
    private val marketPreferences: MarketPreferences,
    errorHandler: ErrorHandler,
    @IODispatcher dispatcher: CoroutineDispatcher
) : CoroutineUseCase<Boolean, Unit>(errorHandler, dispatcher) {

    override suspend fun execute(params: Boolean) {
        marketPreferences.enableNotification(isEnabled = params)
    }
}