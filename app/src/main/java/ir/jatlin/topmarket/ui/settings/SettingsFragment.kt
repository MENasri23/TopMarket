package ir.jatlin.topmarket.ui.settings

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
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

    private lateinit var listAdapter: ArrayAdapter<Int>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        collectUiStates()


    }

    private fun initViews() {
        binding.viewModel = viewModel
        listAdapter = ArrayAdapter(requireContext(), R.layout.layout_settings_hour_list_item)


        binding.tvHour.apply {
            setAdapter(listAdapter)

            setOnItemClickListener { _, _, position, _ ->
                listAdapter.getItem(position)?.let { interval ->
                    viewModel.setInterval(interval)
                }
            }
        }

    }

    private fun collectUiStates() = repeatOnViewLifecycleOwner {

        loadStateViewModel.stopLoading()


        launch {
            viewModel.preSetNotificationIntervals.collectLatest {
                listAdapter.clear()
                listAdapter.addAll(it)
            }
        }

        launch {
            viewModel.notificationInterval.collectLatest {
                binding.tvHour.setText(it.toString(), false)
                binding.tvHour.setSelection(binding.tvHour.text.length)
            }
        }


    }


}