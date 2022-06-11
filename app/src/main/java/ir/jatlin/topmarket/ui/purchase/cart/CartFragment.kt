package ir.jatlin.topmarket.ui.purchase.cart

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.databinding.FragmentCartBinding
import ir.jatlin.topmarket.ui.util.viewBinding

class CartFragment : Fragment(R.layout.fragment_cart) {

    private val viewModel by viewModels<CartViewModel>()
    private val binding by viewBinding(FragmentCartBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        collectUiStates()
    }

    private fun initViews() {
        binding.cartOrderItems
    }

    private fun collectUiStates() {

    }
}