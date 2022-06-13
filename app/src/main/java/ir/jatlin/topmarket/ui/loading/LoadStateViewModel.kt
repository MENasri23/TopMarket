package ir.jatlin.topmarket.ui.loading

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoadStateViewModel @Inject constructor(
) : ViewModel() {


    private val _loading = MutableSharedFlow<Boolean>()
    val loading = _loading.asSharedFlow()

    suspend fun startLoading() {
        Timber.d("Start loading")
        _loading.emit(true)
    }

    suspend fun stopLoading() {
        Timber.d("Stop loading")
        _loading.emit(false)
    }
}