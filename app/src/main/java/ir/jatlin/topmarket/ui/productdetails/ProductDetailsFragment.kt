package ir.jatlin.topmarket.ui.productdetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.core.shared.Resource
import ir.jatlin.topmarket.databinding.FragmentProductDetailsBinding
import ir.jatlin.topmarket.ui.util.dataBindings
import ir.jatlin.topmarket.ui.util.executePending
import ir.jatlin.topmarket.ui.util.repeatOnViewLifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailsFragment : Fragment(R.layout.fragment_product_details) {


    private val viewModel by viewModels<ProductDetailsViewModel>()
    private val binding by dataBindings(FragmentProductDetailsBinding::bind)

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
                        viewModel.toggleFavorite()
                    }
                    else -> isHandled = false
                }
                isHandled
            }

            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }


    }

    private fun collectUiStates() = repeatOnViewLifecycleOwner {
        launch {
            collectProductDetails()
        }
        collectProductDependents()
    }

    private fun CoroutineScope.collectProductDependents() = launch {
        viewModel.addToCartCount.collect {
            /* Update the badge in future */
        }
    }


    private suspend fun collectProductDetails() {
        viewModel.productDetails.collect { state ->
            when (state) {
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    val productDetails = state.data!!
                    viewModel.updateUiStatesWith(productDetails)
                    binding.executePending {
                        product = productDetails
                    }
                }
            }
        }
    }


}