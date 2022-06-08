package ir.jatlin.topmarket.ui.signin

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jatlin.topmarket.core.domain.signin.RegisterCustomerByEmailUseCase
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val registerCustomerByEmailUseCase: RegisterCustomerByEmailUseCase
) : ViewModel() {


}