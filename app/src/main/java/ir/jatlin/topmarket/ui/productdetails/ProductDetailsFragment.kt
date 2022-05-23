package ir.jatlin.topmarket.ui.productdetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.databinding.FragmentProductDetailsBinding
import ir.jatlin.topmarket.ui.util.dataBindings

@AndroidEntryPoint
class ProductDetailsFragment : Fragment(R.layout.fragment_product_details) {


    private val viewMode by viewModels<ProductDetailsViewModel>()
    private val binding by dataBindings(FragmentProductDetailsBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewMode


    }


}