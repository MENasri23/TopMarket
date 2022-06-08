package ir.jatlin.topmarket.ui.signin

import android.os.Bundle
import android.view.View
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.databinding.FragmentSignInBinding
import ir.jatlin.topmarket.ui.util.bottomInsets
import ir.jatlin.topmarket.ui.util.dataBindings
import ir.jatlin.topmarket.ui.util.doOnApplyWindowInsets

class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private val binding by dataBindings(FragmentSignInBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root

        binding.root.doOnApplyWindowInsets { v, insets, padding, margins ->
            val insetBottom = insets.bottomInsets()
            v.updatePadding(bottom = insetBottom + padding.bottom)
            insets
        }
    }
}