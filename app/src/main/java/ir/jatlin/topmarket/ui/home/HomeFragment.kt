package ir.jatlin.topmarket.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.core.network.model.product.NetworkProduct
import ir.jatlin.topmarket.core.shared.Resource
import ir.jatlin.topmarket.databinding.FragmentHomeBinding
import ir.jatlin.topmarket.ui.home.category.asProductItem
import ir.jatlin.topmarket.ui.util.dataBindings
import ir.jatlin.topmarket.ui.util.repeatOnViewLifecycleOwner
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel by viewModels<HomeViewModel>()
    private val binding by dataBindings(FragmentHomeBinding::bind)

    private lateinit var productCategoriesAdapter: ProductCategoriesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root

        initViews()
        collectStates()


    }

    private fun initViews() = binding.apply {
        binding.viewModel = viewModel

        productCategoriesAdapter = ProductCategoriesAdapter()
        productCategories.adapter = productCategoriesAdapter

    }

    private fun collectStates() = repeatOnViewLifecycleOwner {
        launch { collectHomeUiState() }
    }

    private suspend fun collectHomeUiState() = viewModel.homeUiState.collect { stateResult ->
        when (stateResult) {
            is Resource.Error -> {
                Timber.e(stateResult.cause.toString())
            }
            is Resource.Loading -> { Timber.d("loading") }
            is Resource.Success -> {
                val productCategories = stateResult.data!!.categorizedProducts
                productCategoriesAdapter.submitList(
                    productCategories.map { categoryState ->
                        ProductCategoriesItem.CategoryItem(
                            label = getString(categoryState.label),
                            data = categoryState.products.map(NetworkProduct::asProductItem)
                        )
                    }
                )
            }
        }
    }

}