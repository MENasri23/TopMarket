package ir.jatlin.topmarket.ui.productdetails

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.PagerSnapHelper
import dagger.hilt.android.AndroidEntryPoint
import ir.jatlin.core.model.product.Product
import ir.jatlin.core.shared.Resource
import ir.jatlin.core.shared.isSuccess
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.databinding.FragmentProductDetailsBinding
import ir.jatlin.topmarket.ui.loading.LoadStateViewModel
import ir.jatlin.topmarket.ui.product.ProductDisplayAdapter
import ir.jatlin.topmarket.ui.product.ProductItemEventListener
import ir.jatlin.topmarket.ui.product.asProductItem
import ir.jatlin.topmarket.ui.slider.SliderAdapter
import ir.jatlin.topmarket.ui.slider.SliderItem
import ir.jatlin.topmarket.ui.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailsFragment : Fragment(R.layout.fragment_product_details),
    ProductItemEventListener {

    private val loadStateViewModel by activityViewModels<LoadStateViewModel>()
    private val viewModel by viewModels<ProductDetailsViewModel>()
    private val binding by dataBindings(FragmentProductDetailsBinding::bind)

    private lateinit var similarProductsAdapter: ProductDisplayAdapter
    private lateinit var imageSliderAdapter: SliderAdapter
    private lateinit var productReviewAdapter: ProductReviewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        initViews()
        collectUiStates()

    }

    private fun initViews() {
        binding.includeAppBar.toolbar.apply {

            setOnMenuItemClickListener {
                var isHandled = true
                when (it.itemId) {
                    R.id.action_more -> {
                        /* Open bottom dialog */
                    }
                    R.id.action_shopping_card -> {
                        /* navigate to checkout cart state */
                    }
                    R.id.action_like -> {
                        this@ProductDetailsFragment.viewModel.toggleFavorite()
                    }
                    else -> isHandled = false
                }
                isHandled
            }

            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }

        binding.similarProducts.apply {
            similarProductsAdapter = ProductDisplayAdapter(
                this@ProductDetailsFragment
            )
            adapter = similarProductsAdapter
            addItemDecoration(
                DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL)
            )
        }

        binding.productImageSlider.apply {
            imageSliderAdapter = SliderAdapter()
            adapter = imageSliderAdapter
            addItemDecoration(
                SpacerItemDecoration(
                    resources.getDimensionPixelOffset(R.dimen.space_medium),
                    SpacerItemDecoration.HORIZONTAL
                )
            )
            PagerSnapHelper().attachToRecyclerView(this)
        }

        productReviewAdapter = ProductReviewAdapter(
            onEachProductReviewCLick = ::navigateToProductReviews
        )
        binding.productReviews.apply {
            adapter = productReviewAdapter
        }

    }

    private fun collectUiStates() = repeatOnViewLifecycleOwner {
        launch {
            collectProductDetails()
        }
        collectProductDependents()
    }

    private fun CoroutineScope.collectProductDependents() {
        launch {
            viewModel.onLoadingQuantity.collect { quantityOnLoading ->
                with(binding.orderQuantityLoading) {
                    if (quantityOnLoading) {
                        visible()
                        playAnimation()
                    } else {
                        gone()
                        pauseAnimation()
                    }
                }
            }
        }
        launch {
            collectOnSuccess(viewModel.similarProducts) { similarProducts ->
                similarProductsAdapter.submitList(
                    similarProducts.map(Product::asProductItem)
                )
            }
        }

        launch {
            viewModel.productImages.collect { images ->
                if (!images.isNullOrEmpty()) {
                    imageSliderAdapter.submitList(
                        images.map { SliderItem.ImageItem(it) }
                    )
                }
            }
        }

        launch {
            viewModel.productReviewsState.collect {
                if (it.isSuccess) {
                    with(binding.productReviewContainer) {
                        isVisible = !it.data.isNullOrEmpty()
                        if (isVisible) {
                            binding.tvReviewsCount.text =
                                getString(R.string.user_comments_count, it.data!!.size)
                        }

                    }
                    productReviewAdapter.submitList(it.data)
                }
            }
        }
    }


    private suspend fun collectProductDetails() {
        viewModel.productDetailsState.collect { state ->
            when (state) {
                is Resource.Error -> {
                    loadStateViewModel.stopLoading()
                    showErrorMessage(state.cause)
                }
                is Resource.Loading -> {
                    loadStateViewModel.startLoading()
                }
                is Resource.Success -> {
                    loadStateViewModel.stopLoading()
                    val productDetails = state.data!!
                    viewModel.updateUiStatesWith(productDetails)
                }
            }
        }
    }

    private fun navigateToProductReviews() {
        // TODO: navigate to product reviews screen
    }

    override fun onProductClick(productId: Int) {
        // todo: navigate to see details of the product with this id
    }


}