package ir.jatlin.topmarket.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jatlin.topmarket.core.domain.settings.EnableNotificationUseCase
import ir.jatlin.topmarket.core.domain.settings.GetNotificationEnabledUseCase
import ir.jatlin.topmarket.core.domain.settings.SetThemeUseCase
import ir.jatlin.topmarket.core.model.Theme
import ir.jatlin.topmarket.core.shared.dataOnSuccessOr
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val setThemeUseCase: SetThemeUseCase,
    private val enableNotificationUseCase: EnableNotificationUseCase,
    getNotificationEnabledUseCase: GetNotificationEnabledUseCase
) : ViewModel() {

    private val _notificationEnabled = MutableStateFlow(true)
    val notificationEnabled = _notificationEnabled.asStateFlow()


    init {
        viewModelScope.launch {
            getNotificationEnabledUseCase(Unit).collect {
                Timber.d("Notification is enabled: $it")
                _notificationEnabled.value = it.dataOnSuccessOr(true)
            }
        }


    }


    fun enableNotification(isEnabled: Boolean) {
        viewModelScope.launch {
            enableNotificationUseCase(isEnabled)
        }
    }

    fun setTheme(theme: Theme) {
        viewModelScope.launch {
            setThemeUseCase(theme)
        }
    }


}
