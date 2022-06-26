package ir.jatlin.topmarket.ui.map

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(

) : ViewModel() {

    private val _location = MutableStateFlow<LatLng?>(null)
    val location = _location.asStateFlow()

    var zoomLevel = DEFAULT_ZOOM_LEVEL


    fun saveCurrentLocation(location: LatLng) {
        _location.value = location
    }


    companion object {
        private const val DEFAULT_ZOOM_LEVEL = 15f
    }

}