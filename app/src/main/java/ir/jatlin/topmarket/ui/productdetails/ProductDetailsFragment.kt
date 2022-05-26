package ir.jatlin.topmarket.ui.productdetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import dagger.hilt.android.AndroidEntryPoint
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.core.network.model.product.NetworkProduct
import ir.jatlin.topmarket.core.shared.Resource
import ir.jatlin.topmarket.databinding.FragmentProductDetailsBinding
import ir.jatlin.topmarket.ui.home.category.ProductCategoryAdapter
import ir.jatlin.topmarket.ui.home.category.asProductItem
import ir.jatlin.topmarket.ui.listener.ProductItemEventListener
import ir.jatlin.topmarket.ui.slider.SliderAdapter
import ir.jatlin.topmarket.ui.slider.SliderItem
import ir.jatlin.topmarket.ui.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailsFragment : Fragment(R.layout.fragment_product_details),
    ProductItemEventListener {

    private val viewModel by viewModels<ProductDetailsViewModel>()
    private val binding by dataBindings(FragmentProductDetailsBinding::bind)

    private lateinit var similarProductsAdapter: ProductCategoryAdapter
    private lateinit var imageSliderAdapter: SliderAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        initViews()
        collectUiStates()

    }

    private fun initViews() = binding.apply {
        includeAppBar.toolbar.apply {

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

        similarProducts.apply {
            similarProductsAdapter = ProductCategoryAdapter(
                this@ProductDetailsFragment
            )
            adapter = similarProductsAdapter
            addItemDecoration(
                DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL)
            )
        }

        productImageSlider.apply {
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


    }

    private fun collectUiStates() = repeatOnViewLifecycleOwner {
        launch {
            collectProductDetails()
        }
        collectProductDependents()
    }

    private fun CoroutineScope.collectProductDependents() {
        launch {
            viewModel.addToCartCount.collect {
                /* Update the badge in future */
            }
        }
        launch {
            collectOnSuccess(viewModel.similarProducts) { similarProducts ->
                similarProductsAdapter.submitList(
                    similarProducts.map(NetworkProduct::asProductItem)
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
    }


    private suspend fun collectProductDetails() {
        viewModel.productDetailsState.collect { state ->
            when (state) {
                is Resource.Error -> {
                    showErrorMessage(state.cause)
                }
                is Resource.Loading -> {}
                is Resource.Success -> {
                    val productDetails = state.data!!
                    viewModel.updateUiStatesWith(productDetails)
                }
            }
        }
    }

    override fun onProductClick(productId: Int) {
        // todo: navigate to see details of the product with this id
    }


}