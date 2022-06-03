package ir.jatlin.topmarket.ui.search.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.core.domain.param.DiscoverParameters
import ir.jatlin.topmarket.databinding.FragmentSortingBinding
import ir.jatlin.topmarket.ui.loading.LoadSateViewModel
import ir.jatlin.topmarket.ui.search.Order
import ir.jatlin.topmarket.ui.search.OrderBy
import ir.jatlin.topmarket.ui.search.SearchViewModel

class SortingFragment : BottomSheetDialogFragment() {

    private val searchViewModel by hiltNavGraphViewModels<SearchViewModel>(R.id.search_graph)
    private var _binding: FragmentSortingBinding? = null
    val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSortingBinding
            .inflate(layoutInflater, container, false)
        binding.viewModel = searchViewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val onSortItemCLickListener = View.OnClickListener {
            with(binding) {
                val orderBy = when (it?.id) {
                    mostSales.id -> OrderBy.Popularity
                    mostExpensive.id -> OrderBy.Price
                    cheapest.id -> OrderBy.Price.also {
                        searchViewModel.order = Order.Asc
                    }
                    else -> OrderBy.Date
                }
                searchViewModel.orderBy = orderBy
                searchViewModel.searchProducts()
                dismiss()
            }
        }

        with(binding) {
            mostSales.setOnClickListener(onSortItemCLickListener)
            mostExpensive.setOnClickListener(onSortItemCLickListener)
            cheapest.setOnClickListener(onSortItemCLickListener)
            newest.setOnClickListener(onSortItemCLickListener)
        }
    }

}