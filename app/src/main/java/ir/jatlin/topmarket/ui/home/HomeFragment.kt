package ir.jatlin.topmarket.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.databinding.FragmentHomeBinding
import ir.jatlin.topmarket.ui.util.dataBindings

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val viewModel by viewModels<HomeViewModel>()
    private val binding by dataBindings(FragmentHomeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

    }

}