package ir.jatlin.topmarket.ui.search

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
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
                viewModel.searchResult.collect {
                    Timber.d(it.size.toString())

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

        includeSearchBar.searchView.apply {
            setOnQueryTextFocusChangeListener { v, hasFocus ->
                if (hasFocus) v.findFocus().showKeyboard()
            }

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    hideKeyboard()
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.onSearchTextChanged(newText)
                    return true
                }
            })
        }

    }


}