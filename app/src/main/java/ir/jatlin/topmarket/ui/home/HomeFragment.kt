package ir.jatlin.topmarket.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.databinding.FragmentHomeBinding
import ir.jatlin.topmarket.ui.home.slider.SpecialProductGroupViewHolder
import ir.jatlin.topmarket.ui.loading.LoadSateViewModel
import ir.jatlin.topmarket.ui.product.ProductDisplayGroupEventListener
import ir.jatlin.topmarket.ui.product.ProductItemEventListener
import ir.jatlin.topmarket.ui.util.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment :
    Fragment(R.layout.fragment_home),
    ProductDisplayGroupEventListener,
    ProductItemEventListener,
    SpecialProductGroupViewHolder.EventListener {
    private val loadStateViewModel by activityViewModels<LoadSateViewModel>()
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
            this@HomeFragment,
            this@HomeFragment
        )

        productCategories.addItemDecoration(
            SpacerItemDecoration(
                resources.getDimensionPixelOffset(R.dimen.space_medium)
            )
        )
        productCategories.adapter = homeDisplayItemAdapter


        includeSearchBar.apply {
            val onSearchListener = View.OnClickListener {
                findNavController().navigate(
                    HomeFragmentDirections.toSearchFragment()
                )
            }
            tvSearch.setOnClickListener(onSearchListener)
            ivSearch.setOnClickListener(onSearchListener)
        }

    }

    private fun collectStates() = repeatOnViewLifecycleOwner {
        loadStateViewModel.startLoading()
        launch { collectHomeUiState() }
    }

    private suspend fun collectHomeUiState() = viewModel.homeUiState.safeCollect(
        onFailure = {
            loadStateViewModel.stopLoading()
            showErrorMessage(it) {
                // TODO: reload result
            }
        }
    ) { items ->
        loadStateViewModel.stopLoading()
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

    override fun onSliderItemPositionChanged(currentPosition: Int) {
        viewModel.onSliderItemPositionChanged(currentPosition)
    }

    override fun getCurrentSpecialProductItemPosition(): Int {
        return viewModel.sliderItemPosition.value
    }

}