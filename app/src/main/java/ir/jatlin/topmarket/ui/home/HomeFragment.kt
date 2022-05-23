package ir.jatlin.topmarket.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.core.shared.Resource
import ir.jatlin.topmarket.databinding.FragmentHomeBinding
import ir.jatlin.topmarket.ui.util.dataBindings
import ir.jatlin.topmarket.ui.util.repeatOnViewLifecycleOwner
import kotlinx.coroutines.launch

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

        launch {
            viewModel.homeUiState.collect { stateResult ->
                when (stateResult) {
                    is Resource.Error -> {}
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        // TODO: Show result 
                    }
                }
            }
        }
    }

}