package ir.jatlin.topmarket.ui.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.databinding.FragmentSearchBinding
import ir.jatlin.topmarket.ui.util.dataBindings

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {


    private val viewModel by viewModels<SearchViewModel>()
    private val binding by dataBindings(FragmentSearchBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root // TODO: Remove later
    }


}