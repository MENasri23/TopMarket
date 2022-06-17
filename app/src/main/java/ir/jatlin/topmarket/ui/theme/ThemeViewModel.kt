package ir.jatlin.topmarket.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jatlin.core.shared.dataOnSuccessOr
import ir.jatlin.core.shared.isSuccess
import ir.jatlin.core.shared.theme.ThemeUtils
import ir.jatlin.topmarket.core.domain.settings.GetThemeStreamUseCase
import ir.jatlin.topmarket.core.domain.settings.GetThemeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val getThemeUseCase: GetThemeUseCase,
    private val getThemeStreamUseCase: GetThemeStreamUseCase,
) : ViewModel() {

    val currentTheme = runBlocking {
        getThemeUseCase(Unit).dataOnSuccessOr(ThemeUtils.defaultTheme())
    }

    private val _selectedTheme = MutableStateFlow(currentTheme)
    val selectedTheme = _selectedTheme.asStateFlow()

    init {
        viewModelScope.launch {
            getThemeStreamUseCase(Unit).collect {
                if (it.isSuccess) {
                    _selectedTheme.value = it.data!!
                }
            }
        }
    }
}