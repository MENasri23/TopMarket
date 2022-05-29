package ir.jatlin.topmarket.ui.search

import android.graphics.Rect
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.view.doOnLayout
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.databinding.FragmentSearchBinding
import ir.jatlin.topmarket.ui.util.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {


    private val viewModel by viewModels<SearchViewModel>()
    private val binding by dataBindings(FragmentSearchBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        collectUiStates()
    }

    private fun collectUiStates() {
        repeatOnViewLifecycleOwner {
            launch {
                viewModel.searchResult.collect { result ->
                    Timber.tag("collectUiStates").d(result.joinToString { it.productName })

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
            setOnEditorActionListener { _, actionId, event ->
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

    }


}