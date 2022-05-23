package ir.jatlin.topmarket.ui.productdetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.core.shared.Resource
import ir.jatlin.topmarket.databinding.FragmentProductDetailsBinding
import ir.jatlin.topmarket.ui.util.dataBindings
import ir.jatlin.topmarket.ui.util.repeatOnViewLifecycleOwner
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailsFragment : Fragment(R.layout.fragment_product_details) {


    private val viewModel by viewModels<ProductDetailsViewModel>()
    private val binding by dataBindings(FragmentProductDetailsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        initViews()

    }

    private fun initViews() = binding.apply {

    }

    private fun collectUiStates() = repeatOnViewLifecycleOwner {
        launch {
            collectProductDetails()
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
                }
            }
        }
    }


}