package ir.jatlin.topmarket.core.domain.settings

import ir.jatlin.topmarket.core.data.di.IODispatcher
import ir.jatlin.topmarket.core.data.source.local.datastore.MarketPreferences
import ir.jatlin.topmarket.core.domain.CoroutineUseCase
import ir.jatlin.topmarket.core.model.Theme
import ir.jatlin.topmarket.core.shared.fail.ErrorHandler
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