package ir.jatlin.topmarket.ui.signin

import android.os.Bundle
import android.view.View
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.databinding.FragmentSignInBinding
import ir.jatlin.topmarket.ui.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private val viewModel by viewModels<SignInViewModel>()
    private val binding by dataBindings(FragmentSignInBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch(Dispatchers.Main.immediate) {
            viewModel.currentCustomerId.collectLatest {
                it?.let {
                    Timber.d("current user id: $it")
                    // TODO: navigate to profile screen
                }
            }
        }
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
}