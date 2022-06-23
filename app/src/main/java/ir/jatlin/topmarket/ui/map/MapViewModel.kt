package ir.jatlin.topmarket.ui.map

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(

) : ViewModel() {

    var zoomLevel = DEFAULT_ZOOM_LEVEL


    companion object {
        private const val DEFAULT_ZOOM_LEVEL = 15f
    }

}