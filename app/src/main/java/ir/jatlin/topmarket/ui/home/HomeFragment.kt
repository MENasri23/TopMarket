package ir.jatlin.topmarket.ui.home

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.core.shared.isSuccess
import ir.jatlin.topmarket.databinding.FragmentHomeBinding
import ir.jatlin.topmarket.ui.product.ProductDisplayGroupEventListener
import ir.jatlin.topmarket.ui.product.ProductItemEventListener
import ir.jatlin.topmarket.ui.util.dataBindings
import ir.jatlin.topmarket.ui.util.repeatOnViewLifecycleOwner
import ir.jatlin.topmarket.ui.util.safeCollect
import ir.jatlin.topmarket.ui.util.showErrorMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class HomeFragment :
    Fragment(R.layout.fragment_home),
    ProductDisplayGroupEventListener,
    ProductItemEventListener {

    private val viewModel by viewModels<HomeViewModel>()
    private val binding by dataBindings(FragmentHomeBinding::bind)

    private lateinit var homeDisplayItemAdapter: HomeDisplayItemAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root

        initViews()
        collectStates()


    }

    private fun initViews() = binding.apply {
        viewModel = this@HomeFragment.viewModel

        homeDisplayItemAdapter = HomeDisplayItemAdapter(
            this@HomeFragment,
            this@HomeFragment
        )
        productCategories.adapter = homeDisplayItemAdapter

    }

    private fun collectStates() = repeatOnViewLifecycleOwner {
        launch { collectHomeUiState() }
    }

    private suspend fun collectHomeUiState() = viewModel.homeUiState.safeCollect(
        onFailure = { showErrorMessage(it) }
    ) { items ->
        homeDisplayItemAdapter.submitList(items.homeDisplayItems)
    }

    override fun onShowMoreClick() {
        /* Show more products */
    }

    override fun onProductClick(productId: Int) {
        val action = HomeFragmentDirections
            .actionHomeFragmentToProductDetailsFragment(productId)
        findNavController().navigate(action)
    }

}