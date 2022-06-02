package ir.jatlin.topmarket.ui.search.filter

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.databinding.FragmentSearchFiltersBinding
import ir.jatlin.topmarket.ui.loading.LoadSateViewModel
import ir.jatlin.topmarket.ui.product.preview.ProductPreviewAdapter
import ir.jatlin.topmarket.ui.search.SearchFragmentDirections
import ir.jatlin.topmarket.ui.search.SearchViewModel
import ir.jatlin.topmarket.ui.util.repeatOnViewLifecycleOwner
import ir.jatlin.topmarket.ui.util.safeCollect
import ir.jatlin.topmarket.ui.util.showErrorMessage
import ir.jatlin.topmarket.ui.util.viewBinding
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SearchFiltersFragment : Fragment(R.layout.fragment_search_filters) {

    private val loadStateViewModel by activityViewModels<LoadSateViewModel>()
    private val searchViewModel by hiltNavGraphViewModels<SearchViewModel>(R.id.search_graph)

    private val binding by viewBinding(FragmentSearchFiltersBinding::bind)

    private val args by navArgs<SearchFiltersFragmentArgs>()

    private lateinit var productPreviewAdapter: ProductPreviewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        collectUiStates()


        val categoryId = args.categoryId
        searchViewModel.searchProductsWith(categoryId)

    }

    private fun initViews() = binding.apply {

        filterAppbar.apply {

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

        }
    }

    private fun collectUiStates() = repeatOnViewLifecycleOwner {

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
            onFailure = { showErrorMessage(it) }
        ) { productsInCategory ->
            productPreviewAdapter.submitList(productsInCategory)
            loadStateViewModel.stopLoading()
            Timber.tag("SearchFilterFragment").d("${productsInCategory.size}")
        }
    }

    private fun navigateToDetailsScreen(productId: Int) {
        findNavController().navigate(
            R.id.productDetailsFragment, bundleOf("productId" to productId)
        )
    }

}