package ir.jatlin.topmarket.core.domain.settings

import ir.jatlin.core.model.Theme
import ir.jatlin.topmarket.core.data.di.IODispatcher
import ir.jatlin.topmarket.core.data.source.local.datastore.MarketPreferences
import ir.jatlin.topmarket.core.domain.FlowUseCase
import ir.jatlin.topmarket.core.shared.fail.ErrorHandler
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