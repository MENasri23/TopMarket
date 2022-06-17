package ir.jatlin.topmarket.ui.categories

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jatlin.core.domain.util.makeCategoryParams
import ir.jatlin.core.model.category.CategoryDetails
import ir.jatlin.topmarket.ui.util.stateFlow
import javax.inject.Inject

@HiltViewModel
class ProductCategoryViewModel @Inject constructor(
    private val fetchCategoryListUseCase: ir.jatlin.core.domain.category.FetchCategoryDetailsListUseCase
) : ViewModel() {


    val categories = stateFlow<List<CategoryDetails>> {
        fetchCategoryListUseCase(
            params = makeCategoryParams {
                page = 1
                pageSize = 100
            }
        )
    }


}