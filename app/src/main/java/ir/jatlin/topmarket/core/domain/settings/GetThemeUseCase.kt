package ir.jatlin.topmarket.core.domain.settings

import ir.jatlin.core.model.Theme
import ir.jatlin.core.shared.fail.ErrorHandler
import ir.jatlin.core.shared.theme.ThemeUtils
import ir.jatlin.topmarket.core.data.di.IODispatcher
import ir.jatlin.topmarket.core.data.source.local.datastore.MarketPreferences
import ir.jatlin.topmarket.core.domain.CoroutineUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class GetThemeUseCase @Inject constructor(
    private val marketPreferences: MarketPreferences,
    errorHandler: ErrorHandler,
    @IODispatcher dispatcher: CoroutineDispatcher
) : CoroutineUseCase<Unit, Theme>(errorHandler, dispatcher) {

    override suspend fun execute(params: Unit): Theme {
        return marketPreferences.selectedTheme.firstOrNull()
            ?: ThemeUtils.defaultTheme()
    }
}