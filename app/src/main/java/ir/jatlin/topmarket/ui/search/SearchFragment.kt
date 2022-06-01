package ir.jatlin.topmarket.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.inputmethod.EditorInfo
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.doOnLayout
import androidx.core.view.updatePadding
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.internal.ViewUtils
import dagger.hilt.android.AndroidEntryPoint
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.databinding.FragmentSearchBinding
import ir.jatlin.topmarket.ui.util.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    companion object {
        private const val NO_CATEGORY_ID = -1
    }

    private val viewModel by viewModels<SearchViewModel>()
    private val binding by dataBindings(FragmentSearchBinding::bind)

    private val navArgs by navArgs<SearchFragmentArgs>()

    private lateinit var headerItemAdapter: HeaderItemAdapter
    private lateinit var bodyItemAdapter: BodyItemAdapter


    override fun onAttach(context: Context) {
        super.onAttach(context)
        val categoryId = navArgs.categoryId
        if (categoryId != NO_CATEGORY_ID) {
            navigateToSearchFilterScreen(categoryId)
        }
    }

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

    @SuppressLint("WrongConstant")
    private fun initViews() = binding.apply {
        val etSearch = includeSearchBar.searchEditText
        root.clearFocusOnTouchEvent(etSearch)

        etSearch.apply {
            doOnLayout {
                if (requestFocus()) {
                    showKeyboard()
                }
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

        searchInCategorySuggests.doOnApplyWindowInsets { v, insets, padding, _ ->
            v.updatePadding(
                bottom = padding.bottom + insets.systemWindowInsetBottom
            )
            insets
        }

        bodyItemAdapter = BodyItemAdapter(
            onProductInCategoryClicked = this@SearchFragment::navigateToSearchFilterScreen
        )
        searchInCategorySuggests.adapter = bodyItemAdapter

    }

    private fun navigateToProductDetailsScreen(productId: Int) {
        val action = SearchFragmentDirections.toProductDetailsFragment(productId)
        findNavController().navigate(action)
    }

    private fun navigateToSearchFilterScreen(categoryId: Int) {
        val action = SearchFragmentDirections
            .toSearchFiltersFragment()
        action.categoryId = categoryId
        findNavController().navigate(action)
    }


}