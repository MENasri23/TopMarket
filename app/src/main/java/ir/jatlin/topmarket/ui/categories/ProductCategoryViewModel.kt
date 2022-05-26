package ir.jatlin.topmarket.ui.categories

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.jatlin.topmarket.core.domain.category.FetchCategoryDetailsListUseCase
import ir.jatlin.topmarket.core.domain.param.DiscoverParameters
import ir.jatlin.topmarket.core.domain.util.makeCategoryParams
import ir.jatlin.topmarket.ui.util.stateFlow
import javax.inject.Inject

@HiltViewModel
class ProductCategoryViewModel @Inject constructor(
    private val fetchCategoryListUseCase: FetchCategoryDetailsListUseCase
) : ViewModel() {


    val categories = stateFlow {
        fetchCategoryListUseCase(
            params = makeCategoryParams {
                page = 1
                pageSize = 100
            }
        )
    }


}