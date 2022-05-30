package ir.jatlin.topmarket.ui.search.filter

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import dagger.hilt.android.AndroidEntryPoint
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.databinding.FragmentSearchFiltersBinding
import ir.jatlin.topmarket.ui.loading.LoadSateViewModel
import ir.jatlin.topmarket.ui.search.SearchViewModel
import ir.jatlin.topmarket.ui.util.repeatOnViewLifecycleOwner
import ir.jatlin.topmarket.ui.util.showErrorMessage
import ir.jatlin.topmarket.ui.util.viewBinding
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class SearchFiltersFragment : Fragment(R.layout.fragment_search_filters) {

    private val loadStateViewModel by activityViewModels<LoadSateViewModel>()
    private val searchViewModel by hiltNavGraphViewModels<SearchViewModel>(R.id.main_graph_xml)

    private val binding by viewBinding(FragmentSearchFiltersBinding::bind)

    private val args by navArgs<SearchFiltersFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        collectUiStates()


        val categoryId = args.categoryId
        searchViewModel.searchProductsWith(categoryId)

    }

    private fun initViews() = binding.apply {
        root
    }

    private fun collectUiStates() = repeatOnViewLifecycleOwner {
        loadStateViewModel.startLoading()
        launch {
            searchViewModel.productsInCategory.collect { items ->
                loadStateViewModel.stopLoading()
                Timber.tag("SearchFilterFragment").d("${items?.size}")
            }
        }

        launch {
            searchViewModel.error.collect { cause ->
                loadStateViewModel.stopLoading()
                showErrorMessage(cause)

            }
        }
    }


}