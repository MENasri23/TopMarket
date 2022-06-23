package ir.jatlin.topmarket.ui.map

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import timber.log.Timber

class LocationPermissionManager(
    private val registry: ActivityResultRegistry,
    private val onDenied: (deniedPermissions: Set<String>) -> Unit = {},
    private val onGranted: () -> Unit = {}
) : DefaultLifecycleObserver {

    private lateinit var locationPermissions: ActivityResultLauncher<Array<String>>

    override fun onCreate(owner: LifecycleOwner) {
        locationPermissions = registry.register(
            KEY_LOCATION_PERMISSIONS,
            owner,
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            it.forEach { (permission, isGranted) ->
                Timber.d("location permission state: ($permission, $isGranted)")
            }
            when {
                it.any { (_, isGranted) -> isGranted } -> onGranted()
                else -> {
                    val deniedPermissions = it
                        .filterNot(Map.Entry<*, Boolean>::value)
                        .keys
                    onDenied(deniedPermissions)
                }
            }

        }
    }

    fun requestLocationPermissions(context: Context?) {
        context ?: return
        if (isPermissionGranted(context)) return

        locationPermissions.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    fun isPermissionGranted(context: Context?): Boolean {
        context ?: return false
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    }


    companion object {
        private const val KEY_LOCATION_PERMISSIONS = "locationPermissionsKey"
    }
}

