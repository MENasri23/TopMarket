package ir.jatlin.topmarket.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.databinding.FragmentSettingsBinding
import ir.jatlin.topmarket.ui.loading.LoadSateViewModel
import ir.jatlin.topmarket.ui.util.dataBindings
import ir.jatlin.topmarket.ui.util.repeatOnViewLifecycleOwner
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val loadStateViewModel by activityViewModels<LoadSateViewModel>()
    private val viewModel by viewModels<SettingsViewModel>()
    private val binding by dataBindings(FragmentSettingsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        collectUiStates()


    }

    private fun initViews() {
        binding.viewModel = viewModel

    }

    private fun collectUiStates() = repeatOnViewLifecycleOwner {

        loadStateViewModel.stopLoading()


    }


}