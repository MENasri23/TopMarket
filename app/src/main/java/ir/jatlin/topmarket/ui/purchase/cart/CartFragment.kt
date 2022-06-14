package ir.jatlin.topmarket.ui.purchase.cart

import android.os.Bundle
import android.transition.Transition
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.databinding.FragmentCartBinding
import ir.jatlin.topmarket.ui.loading.LoadStateViewModel
import ir.jatlin.topmarket.ui.util.dataBindings
import ir.jatlin.topmarket.ui.util.repeatOnViewLifecycleOwner
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class CartFragment : Fragment(R.layout.fragment_cart) {

    private val loadStateViewModel by activityViewModels<LoadStateViewModel>()
    private val viewModel by viewModels<CartViewModel>()
    private val binding by dataBindings(FragmentCartBinding::bind)

    private lateinit var cartProductAdapter: CartProductAdapter

    private lateinit var toggle: Transition

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        collectUiStates()
    }

    private fun initViews() {
        binding.cartViewModel = viewModel

        toggle = TransitionInflater.from(context)
            .inflateTransition(R.transition.discount_toggle)

        cartProductAdapter = CartProductAdapter()
        binding.cartOrderItems.apply {
            adapter = cartProductAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        binding.discountApply.setOnClickListener {
            val couponCode = binding.discountEnter.text
            viewModel.checkCouponCode(couponCode.toString())
        }

        binding.collapsableDiscountContainer.setOnClickListener {
            toggleDiscountExpanded()
        }
        binding.discountExpandIcon.setOnClickListener {
            toggleDiscountExpanded()
        }

    }

    private fun collectUiStates() = repeatOnViewLifecycleOwner {

        launch {
            viewModel.loading.collect { isLoading ->
                if (isLoading) loadStateViewModel.startLoading()
                else loadStateViewModel.stopLoading()
            }
        }

        launch {
            viewModel.cartProductItems.collect { cartProducts ->
                Timber.d("cart products size: ${cartProducts?.products?.size} ${cartProducts?.products}")
                cartProductAdapter.submitList(cartProducts?.products)
            }
        }

        launch {
            viewModel.discountExpanded.collectLatest { expanded ->
                toggle.duration = if (expanded) 300L else 200L
                TransitionManager.beginDelayedTransition(binding.root.parent as ViewGroup, toggle)
                binding.collapsableDiscountBodyContainer.visibility =
                    if (expanded) View.VISIBLE else View.GONE
                binding.discountExpandIcon.rotationX = if (expanded) 180f else 0f

            }
        }

    }

    private fun toggleDiscountExpanded() {
        viewModel.onToggleDiscountExpand()
    }
}