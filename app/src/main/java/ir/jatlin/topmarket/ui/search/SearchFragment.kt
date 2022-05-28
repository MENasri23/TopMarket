package ir.jatlin.topmarket.ui.search

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.databinding.FragmentSearchBinding
import ir.jatlin.topmarket.ui.util.dataBindings
import ir.jatlin.topmarket.ui.util.hideKeyboard
import ir.jatlin.topmarket.ui.util.repeatOnViewLifecycleOwner
import ir.jatlin.topmarket.ui.util.showKeyboard
import timber.log.Timber

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {


    private val viewModel by viewModels<SearchViewModel>()
    private val binding by dataBindings(FragmentSearchBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        repeatOnViewLifecycleOwner {
            viewModel.searchResult.collect {
                Timber.d(it.size.toString())

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