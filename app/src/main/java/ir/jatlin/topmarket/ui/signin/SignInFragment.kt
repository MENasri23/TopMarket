package ir.jatlin.topmarket.ui.signin

import android.os.Bundle
import android.view.View
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.databinding.FragmentSignInBinding
import ir.jatlin.topmarket.ui.util.bottomInset
import ir.jatlin.topmarket.ui.util.dataBindings
import ir.jatlin.topmarket.ui.util.doOnApplyWindowInsets

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private val binding by dataBindings(FragmentSignInBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyBottomInset()

        initViews()
    }

    private fun initViews() = binding.apply {

    }

    fun collectUiStates() {

    }

    private fun applyBottomInset() {
        binding.root.doOnApplyWindowInsets { v, insets, padding, _ ->
            val insetBottom = insets.bottomInset()
            v.updatePadding(bottom = insetBottom + padding.bottom)
            insets
        }
    }
}