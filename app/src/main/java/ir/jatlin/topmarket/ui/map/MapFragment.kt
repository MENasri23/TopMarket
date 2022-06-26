package ir.jatlin.topmarket.ui.map

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.databinding.FragmentMapBinding
import ir.jatlin.topmarket.ui.util.dataBindings
import ir.jatlin.topmarket.ui.util.repeatOnViewLifecycleOwner
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MapFragment : Fragment(R.layout.fragment_map) {

    private val viewModel by viewModels<MapViewModel>()
    private val binding by dataBindings(FragmentMapBinding::bind)

    private lateinit var locationPermissionManager: LocationPermissionManager
    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationPermissionManager = LocationPermissionManager(
            registry = requireActivity().activityResultRegistry,
            onDenied = {
                // TODO: Show a relational ui to explain to the user
                //  the app needs location permission
            }
        ) {
            enableMyLocation()
        }

        lifecycle.addObserver(locationPermissionManager)
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation(requestPermission: Boolean = false) {
        if (locationPermissionManager.isPermissionGranted(context)) {
            mapView.getMapAsync { map ->
                map.isMyLocationEnabled = true
                map.uiSettings.isMyLocationButtonEnabled = true
            }
            return
        }

        if (requestPermission) {
            locationPermissionManager.requestLocationPermissions(context)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = binding.map.apply {
            onCreate(savedInstanceState)
        }


        binding.map.getMapAsync { googleMap ->
            googleMap.apply {
                mapType = GoogleMap.MAP_TYPE_NORMAL

                val tehran = LatLng(35.721953, 51.335267)
                val marker = addMarker(MarkerOptions().position(tehran))
                moveCamera(CameraUpdateFactory.newLatLng(tehran))
                moveCamera(CameraUpdateFactory.zoomTo(15f))

                setOnCameraMoveListener {
                    marker?.position = cameraPosition.target
                    viewModel.zoomLevel = cameraPosition.zoom
                }

            }

            enableMyLocation(true)
        }

        binding.fabAddLocation.setOnClickListener {
            mapView.getMapAsync { map ->
                viewModel.saveCurrentLocation(map.cameraPosition.target)
            }
        }

        collectUiStates()
    }


    fun collectUiStates() {
        repeatOnViewLifecycleOwner {
            launch {
                viewModel.location.collect {
                    it?.let {
                        Toast.makeText(
                            context,
                            "location:(${it.latitude}, ${it.longitude})",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

}