package ir.jatlin.topmarket.ui.search.filter

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.NavGraph
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.databinding.FragmentSearchFiltersBinding
import ir.jatlin.topmarket.ui.loading.LoadStateViewModel
import ir.jatlin.topmarket.ui.product.preview.ProductPreviewAdapter
import ir.jatlin.topmarket.ui.search.SearchViewModel
import ir.jatlin.topmarket.ui.util.*
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SearchFiltersFragment : Fragment(R.layout.fragment_search_filters) {

    private val loadStateViewModel by activityViewModels<LoadStateViewModel>()
    private val searchViewModel by hiltNavGraphViewModels<SearchViewModel>(R.id.search_graph)

    private val binding by viewBinding(FragmentSearchFiltersBinding::bind)

    private val args by navArgs<SearchFiltersFragmentArgs>()

    private lateinit var productPreviewAdapter: ProductPreviewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryId = args.categoryId
        val includeIds = args.includeIds
        searchViewModel.searchProductsWith(categoryId, includeIds)

        initViews()
        collectUiStates()



        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (includeIds != null) {
                        popBackStackToStartDestination()
                    } else findNavController().popBackStack()
                }
            }
        )

    }

    private fun initViews() = binding.apply {

        filterAppbar.apply {

            if (searchViewModel.textQuery.isNotBlank()) {
                tvSearch.text = searchViewModel.textQuery
            }

            sortBy.setOnClickListener {
                findNavController().navigate(R.id.sortingFragment)
            }

            filters.setOnClickListener {
            }
        }

        productsList.apply {
            addItemDecoration(
                DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
            )
            adapter = ProductPreviewAdapter(
                onProductClicked = this@SearchFiltersFragment::navigateToDetailsScreen
            ).also { productPreviewAdapter = it }

            doOnApplyWindowInsets { v, insets, padding, margin ->
                v.updatePadding(
                    bottom = padding.bottom + insets.bottomInset()
                )
                insets
            }

        }
    }

    private fun collectUiStates() = repeatOnViewLifecycleOwner {

        launch {
            searchViewModel.loading.collect { isLoading ->
                if (isLoading) {
                    loadStateViewModel.startLoading()
                } else {
                    loadStateViewModel.stopLoading()
                }
            }
        }

        launch {
            collectProducts()
        }

        launch {
            searchViewModel.error.collect { cause ->
                loadStateViewModel.stopLoading()
                showErrorMessage(cause)
            }
        }
    }

    private suspend fun collectProducts() {
        searchViewModel.productsInCategory.safeCollect(
            onLoading = { loadStateViewModel.startLoading() },
            onFailure = {
                loadStateViewModel.stopLoading()
                showErrorMessage(it) {
                    searchViewModel.searchProducts()
                }
            }
        ) { productsInCategory ->
            productPreviewAdapter.submitList(productsInCategory)
            loadStateViewModel.stopLoading()
            Timber.tag("SearchFilterFragment").d("${productsInCategory.size}")
        }
    }

    private fun navigateToDetailsScreen(productId: Int) {
        findNavController().navigate(
            SearchFiltersFragmentDirections
                .actionSearchFiltersFragmentToProductDetailsFragment(productId)
        )
    }

    private fun popBackStackToStartDestination() {
        val navController = findNavController()
        val mainGraph = navController
            .findDestination(R.id.main_graph) as? NavGraph ?: return
        navController.popBackStack(mainGraph.startDestinationId, false)
    }

}