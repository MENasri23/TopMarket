package ir.jatlin.topmarket.core.domain.settings

import ir.jatlin.core.data.di.IODispatcher
import ir.jatlin.core.data.source.local.datastore.MarketPreferences
import ir.jatlin.core.model.Theme
import ir.jatlin.core.shared.fail.ErrorHandler
import ir.jatlin.topmarket.core.domain.CoroutineUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class SetThemeUseCase @Inject constructor(
    private val marketPreferences: MarketPreferences,
    errorHandler: ErrorHandler,
    @IODispatcher dispatcher: CoroutineDispatcher
) : CoroutineUseCase<Theme, Unit>(errorHandler, dispatcher) {

    override suspend fun execute(params: Theme) {
        marketPreferences.saveTheme(params)
    }
}