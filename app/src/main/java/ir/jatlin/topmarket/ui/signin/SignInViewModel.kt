package ir.jatlin.topmarket.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.core.domain.signin.*
import ir.jatlin.topmarket.core.shared.dataOnSuccessOr
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val registerOrSignInCustomerUseCase: RegisterOrSignInCustomerUseCase,
    private val isValidEmailUseCase: IsValidEmailUseCase,
    getCurrentCustomerIdStreamUseCase: GetCurrentCustomerIdStreamUseCase
) : ViewModel() {


    private val _currentCustomerId = MutableStateFlow<Int?>(null)
    val currentCustomerId = _currentCustomerId.asStateFlow()


    private val _error = MutableStateFlow<Int?>(null)
    val error = _error.asStateFlow()

    init {
        viewModelScope.launch {
            getCurrentCustomerIdStreamUseCase(Unit).collectLatest {
                _currentCustomerId.value = it.dataOnSuccessOr(null)
            }
        }
    }

    fun onRegisterOrSignInCustomer(email: String?) {
        when {
            email.isNullOrBlank() -> _error.value = R.string.error_email_is_empty
            !isValidEmailUseCase(email) -> _error.value = R.string.error_invalid_email
            else -> {
                viewModelScope.launch {
                    registerOrSignInCustomerUseCase(email)
                }

            }
        }

    }

}