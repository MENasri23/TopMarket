package ir.jatlin.topmarket.ui.purchase.cart.shipping

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.ui.signin.SignInFragment
import ir.jatlin.topmarket.ui.util.repeatOnViewLifecycleOwner
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@AndroidEntryPoint
class ShippingFragment : Fragment(R.layout.fragment_shipping) {

    private val viewModel by viewModels<ShippingViewModel>()

    private val args by navArgs<ShippingFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navController = findNavController()
        val currentBackStackEntry = navController.currentBackStackEntry!!
        currentBackStackEntry.savedStateHandle
            .getLiveData<Boolean>(SignInFragment.SIGNED_IN_KEY)
            .observe(this) { isSignedId ->
                if (!isSignedId) {
                    navController.popBackStack(R.id.shippingFragment, true)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repeatOnViewLifecycleOwner(Lifecycle.State.CREATED) {
            viewModel.isSignedIn.collectLatest { signedIn ->
                if (!signedIn) {
                    Timber.d("is signed in: $signedIn")
                    findNavController().navigate(R.id.signInFragment)
                } else {
                    val orderId = args.orderId
                    viewModel.markAsCompleted(orderId)
                }
            }
        }

        if (savedInstanceState != null) {
            viewModel.markAsCompleted(args.orderId)
        }

        initViews()
        collectUiStates()
    }

    private fun initViews() {

    }

    private fun collectUiStates() {
        repeatOnViewLifecycleOwner {
            viewModel.orderCompleted.collect {
                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}