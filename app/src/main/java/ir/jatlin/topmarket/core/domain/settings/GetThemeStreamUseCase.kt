package ir.jatlin.topmarket.core.domain.settings

import ir.jatlin.core.data.di.IODispatcher
import ir.jatlin.core.data.source.local.datastore.MarketPreferences
import ir.jatlin.core.model.Theme
import ir.jatlin.core.shared.fail.ErrorHandler
import ir.jatlin.topmarket.core.domain.FlowUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetThemeStreamUseCase @Inject constructor(
    private val marketPreferences: MarketPreferences,
    errorHandler: ErrorHandler,
    @IODispatcher dispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, Theme>(errorHandler, dispatcher) {

    override fun execute(params: Unit): Flow<Theme> {
        return marketPreferences.selectedTheme
    }
}