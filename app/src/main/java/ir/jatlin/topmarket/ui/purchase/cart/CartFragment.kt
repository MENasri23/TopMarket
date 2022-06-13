package ir.jatlin.topmarket.ui.purchase.cart

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.databinding.FragmentCartBinding
import ir.jatlin.topmarket.ui.loading.LoadStateViewModel
import ir.jatlin.topmarket.ui.util.repeatOnViewLifecycleOwner
import ir.jatlin.topmarket.ui.util.viewBinding
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class CartFragment : Fragment(R.layout.fragment_cart) {

    private val loadStateViewModel by activityViewModels<LoadStateViewModel>()
    private val viewModel by viewModels<CartViewModel>()
    private val binding by viewBinding(FragmentCartBinding::bind)

    private lateinit var cartProductAdapter: CartProductAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        collectUiStates()
    }

    private fun initViews() {

        cartProductAdapter = CartProductAdapter()

        binding.cartOrderItems.apply {
            adapter = cartProductAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }


    }

    private fun collectUiStates() = repeatOnViewLifecycleOwner {

        launch {
            viewModel.loading.collect { }
        }

        launch {
            viewModel.cartProductItems.collect { cartProducts ->
                Timber.d("cart products size: ${cartProducts?.products?.size} ${cartProducts?.products}")
                cartProductAdapter.submitList(cartProducts?.products)
            }
        }

    }
}