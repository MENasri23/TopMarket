package ir.jatlin.topmarket.ui.productdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import ir.jatlin.topmarket.ui.util.stateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.lang.IllegalArgumentException
import javax.inject.Inject

class ProductDetailsViewModel @Inject constructor(

    private val state: SavedStateHandle
) : ViewModel() {

    private val productId = state.getLiveData<Int>("productId").asFlow()


}


