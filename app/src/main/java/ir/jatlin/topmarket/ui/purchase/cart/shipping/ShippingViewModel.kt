package ir.jatlin.topmarket.ui.purchase.cart.shipping

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jatlin.topmarket.core.domain.signin.GetCurrentCustomerIdStreamUseCase
import ir.jatlin.topmarket.core.shared.Resource
import ir.jatlin.topmarket.core.shared.dataOnSuccessOr
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ShippingViewModel @Inject constructor(
    private val getCurrentCustomerIdStreamUseCase: GetCurrentCustomerIdStreamUseCase
) : ViewModel() {

    val customerId = getCurrentCustomerIdStreamUseCase(Unit)

    val isSignedIn = customerId
        .dropWhile { it is Resource.Loading }
        .map {
            it.dataOnSuccessOr(null) != null
        }


}