package ir.jatlin.topmarket.ui.signin

import android.os.Bundle
import android.view.View
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.databinding.FragmentSignInBinding
import ir.jatlin.topmarket.ui.util.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private val viewModel by viewModels<SignInViewModel>()
    private val binding by dataBindings(FragmentSignInBinding::bind)

    private lateinit var savedStateHandle: SavedStateHandle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navController = findNavController()
        savedStateHandle = navController.previousBackStackEntry!!.savedStateHandle
        savedStateHandle.set(SIGNED_IN_KEY, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyBottomInset()

        initViews()
        collectUiStates()

    }

    private fun initViews() = binding.apply {
        registerCustomer.setOnClickListener {
            val email = email.text.toString()
            viewModel.onRegisterOrSignInCustomer(email)
        }
    }

    private fun collectUiStates() = repeatOnViewLifecycleOwner {
        launch {
            viewModel.error.collect {
                it?.let { errorMsg ->
                    Snackbar
                        .make(binding.root, getString(errorMsg), Snackbar.LENGTH_LONG)
                        .show()
                }
            }
        }

        launch {
            viewModel.signedIn.collect { isSignedIn ->
                if (isSignedIn) {
                    savedStateHandle.set(SIGNED_IN_KEY, true)
                    findNavController().popBackStack()
                }
            }
        }
    }

    private fun applyBottomInset() {
        binding.root.doOnApplyWindowInsets { v, insets, padding, _ ->
            val insetBottom = insets.bottomInset()
            val insetTop = insets.topInset()
            v.updatePadding(
                top = padding.top + insetTop,
                right = padding.right,
                left = padding.left,
                bottom = insetBottom + padding.bottom
            )
            insets
        }
    }

    companion object {
        const val SIGNED_IN_KEY = "signed_in_key"
    }
}