package ir.jatlin.topmarket.ui.search

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.databinding.FragmentSearchBinding
import ir.jatlin.topmarket.ui.util.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {


    private val viewModel by viewModels<SearchViewModel>()
    private val binding by dataBindings(FragmentSearchBinding::bind)

    private lateinit var headerItemAdapter: HeaderItemAdapter
    private lateinit var bodyItemAdapter: BodyItemAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        collectUiStates()
    }

    private fun collectUiStates() {
        repeatOnViewLifecycleOwner {
            launch {
                viewModel.searchResult.collect {
                    val searchHeader = it?.header
                    val searchBody = it?.body
                    headerItemAdapter.submitList(searchHeader?.searchProducts)
                    bodyItemAdapter.submitList(searchBody)
                }
            }

            launch {
                viewModel.error.filterNotNull().collectLatest {
                    showErrorMessage(it)
                }
            }
        }
    }

    private fun initViews() = binding.apply {
        val etSearch = includeSearchBar.searchEditText
        root.clearFocusOnTouchEvent(etSearch)

        etSearch.apply {
            if (requestFocus()) {
                showKeyboard()
            }
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    clearFocus()
                    hideKeyboard()
                    true
                } else false
            }

            doOnTextChanged { text, _, _, _ ->
                viewModel.onSearchTextChanged(text?.toString())
            }

        }
        headerItemAdapter = HeaderItemAdapter(
            onProductItemClicked = this@SearchFragment::navigateToProductDetailsScreen
        )
        searchProductSuggests.adapter = headerItemAdapter

        bodyItemAdapter = BodyItemAdapter(
            onProductInCategoryClicked = this@SearchFragment::navigateToSearchFilterScreen
        )
        searchInCategorySuggests.adapter = bodyItemAdapter

    }

    private fun navigateToProductDetailsScreen(productId: Int) {
        val action = SearchFragmentDirections.toProductDetailsFragment(productId)
        findNavController().navigate(action)
    }

    private fun navigateToSearchFilterScreen(categoryId: Int, productId: Int) {
        // TODO: Complete here when filter screen added
    }


}