package ir.jatlin.topmarket.core.domain.settings

import ir.jatlin.core.shared.fail.ErrorHandler
import ir.jatlin.topmarket.core.data.di.IODispatcher
import ir.jatlin.topmarket.core.data.source.local.datastore.MarketPreferences
import ir.jatlin.topmarket.core.domain.CoroutineUseCase
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