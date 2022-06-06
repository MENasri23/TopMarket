package ir.jatlin.topmarket.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jatlin.topmarket.core.domain.settings.GetThemeStreamUseCase
import ir.jatlin.topmarket.core.domain.settings.GetThemeUseCase
import ir.jatlin.topmarket.core.domain.settings.ThemeUtils
import ir.jatlin.topmarket.core.model.Theme
import ir.jatlin.topmarket.core.shared.Resource
import ir.jatlin.topmarket.core.shared.dataOnSuccessOr
import ir.jatlin.topmarket.core.shared.isSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
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