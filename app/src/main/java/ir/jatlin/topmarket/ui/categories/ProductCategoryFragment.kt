package ir.jatlin.topmarket.ui.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.core.network.model.product.category.NetworkCategoryDetails
import ir.jatlin.topmarket.databinding.FragmentProductCategoryBinding
import ir.jatlin.topmarket.ui.util.*
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ProductCategoryFragment : Fragment(R.layout.fragment_product_category),
    ProductCategoryDisplayItemEventListener,
    ProductCategoryDisplayGroupItemEventListener {


    private val viewModel by viewModels<ProductCategoryViewModel>()
    private val binding by viewBinding(FragmentProductCategoryBinding::bind)

    private lateinit var categoryDisplayItemAdapter: CategoryDisplayItemAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root
        viewModel

        initViews()
        collectUiStates()

    }

    private fun initViews() = binding.apply {
        categoryDisplayItemAdapter = CategoryDisplayItemAdapter(
            this@ProductCategoryFragment,
            this@ProductCategoryFragment
        )
        productCategoriesGroup.adapter = categoryDisplayItemAdapter

    }

    private fun collectUiStates() = repeatOnViewLifecycleOwner {
        launch {
            viewModel.categories.safeCollect(
                onLoading = { Timber.d("category loading") },
                onFailure = { showErrorMessage(it) },
            ) { categoryDetails ->
                val categoryGroupItem = categoryDetails.groupBy { it.parentId }
                    .mapKeys { entry ->
                        categoryDetails.find { it.id == entry.key }
                    }
                    .mapNotNull { entry ->
                        val parentName = entry.key?.name ?: return@mapNotNull null
                        CategoryDisplayItem.ProductCategoryGroupItem(
                            label = parentName,
                            categories = entry.value.map(NetworkCategoryDetails::asCategoryItem)
                        )
                    }

                categoryDisplayItemAdapter.submitList(categoryGroupItem)
            }
        }
    }

    override fun onShowMore() {

    }

    override fun onCategoryItemClick(categoryId: Int) {
        // TODO: Navigate to show related products
    }


}