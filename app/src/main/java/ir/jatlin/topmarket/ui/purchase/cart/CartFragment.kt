package ir.jatlin.topmarket.ui.purchase.cart

import android.os.Bundle
import android.transition.Transition
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import ir.jatlin.core.model.product.CartProduct
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.databinding.FragmentCartBinding
import ir.jatlin.topmarket.ui.loading.LoadStateViewModel
import ir.jatlin.topmarket.ui.purchase.PurchaseFragmentDirections
import ir.jatlin.topmarket.ui.util.dataBindings
import ir.jatlin.topmarket.ui.util.gone
import ir.jatlin.topmarket.ui.util.repeatOnViewLifecycleOwner
import ir.jatlin.topmarket.ui.util.visible
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class CartFragment : Fragment(R.layout.fragment_cart), CartProductViewHolder.EventListener {

    private val loadStateViewModel by activityViewModels<LoadStateViewModel>()
    private val viewModel by viewModels<CartViewModel>()
    private val binding by dataBindings(FragmentCartBinding::bind)

    private lateinit var cartProductAdapter: CartProductAdapter

    private lateinit var toggle: Transition
    private lateinit var rootBounds: Transition

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            viewModel.fetchActiveOrder()
        }
        initViews()
        collectUiStates()
    }

    private fun initViews() {
        binding.cartViewModel = viewModel

        with(TransitionInflater.from(context)) {
            toggle = inflateTransition(R.transition.discount_toggle)
            rootBounds = inflateTransition(R.transition.container_change_bounds)
        }

        cartProductAdapter = CartProductAdapter(this)
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

        binding.continuePurchase.setOnClickListener {
            viewModel.onNavigateToShippingScreen()
        }

    }

    private fun navigateToShippingScreen(orderId: Int) {
        findNavController().navigate(
            PurchaseFragmentDirections.toShippingFragment(orderId)
        )
    }

    private fun collectUiStates() = repeatOnViewLifecycleOwner {

        launch {
            viewModel.loading.collect { isLoading ->
                if (isLoading && viewModel.noNestedLoading()) loadStateViewModel.startLoading()
                else loadStateViewModel.stopLoading()
            }
        }

        launch {
            viewModel.cartProductItems.collect { cartProducts ->
                Timber.d("cart products size: ${cartProducts?.products?.size} ${cartProducts?.products}")
                val products = cartProducts?.products
                if (products.isNullOrEmpty()) {
                    binding.root.doOnLayout {
                        TransitionManager.beginDelayedTransition(
                            binding.root as ViewGroup,
                            rootBounds
                        )
                    }
                    binding.purchaseApplyContainer.gone()
                    binding.orderContainer.gone()
                    binding.emptyCartContainer.visible()
                } else {
                    binding.emptyCartContainer.gone()
                    binding.orderContainer.visible()
                    binding.purchaseApplyContainer.visible()
                }
                cartProductAdapter.submitList(products)
                viewModel.stopLoading()
                viewModel.onCartItemLoadingCompleted()

            }
        }

        launch {
            viewModel.discountExpanded.collectLatest { expanded ->
                toggle.duration = if (expanded) 300L else 200L
                binding.root.doOnLayout {
                    TransitionManager.beginDelayedTransition(
                        binding.root.parent as? ViewGroup,
                        toggle
                    )
                }
                binding.collapsableDiscountBodyContainer.visibility =
                    if (expanded) View.VISIBLE else View.GONE
                binding.discountExpandIcon.rotationX = if (expanded) 180f else 0f

            }
        }

        launch {
            viewModel.cartItemLoadingState.collect {
                if (it != null) {
                    val itemLoadingPosition = viewModel.cartItemLoadingPosition ?: return@collect

                    val relatedViewHolder = binding.cartOrderItems
                        .findViewHolderForAdapterPosition(itemLoadingPosition)

                    cartProductAdapter.applyLoadingOn(
                        holder = relatedViewHolder,
                    )
                }

            }
        }

        launch {
            viewModel.orderId.collect {
                it?.let { orderId ->
                    navigateToShippingScreen(orderId)
                    viewModel.onNavigateToShippingScreenCompleted()
                }
            }
        }
    }

    private fun toggleDiscountExpanded() {
        viewModel.onToggleDiscountExpand()
    }

    override fun onAddToOrderClick(cartProduct: CartProduct, position: Int) {
        viewModel.addToCart(cartProduct, position)
    }

    override fun onRemoveFromOrderClick(cartProduct: CartProduct, position: Int) {
        viewModel.removeFromCart(cartProduct, position)
    }

    override fun onCartProductClick(productId: Int) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.d("destroy view")
        viewModel.clearCache()
    }

}