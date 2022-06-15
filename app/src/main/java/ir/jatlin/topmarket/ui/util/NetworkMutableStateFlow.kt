package ir.jatlin.topmarket.ui.util

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber

class NetworkConnectivityFlow constructor(
    private val cm: ConnectivityManager
) : Flow<Boolean> {

    private val _hasNetwork = callbackFlow {
        val networkCallback = createNetworkCallback()
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        Timber.tag(TAG).d("register network callback")
        cm.registerNetworkCallback(networkRequest, networkCallback)

        awaitClose {
            Timber.tag(TAG).d("unregister networkCallback: $networkCallback")
            cm.unregisterNetworkCallback(networkCallback)
        }
    }

    private val TAG by lazy { "NetworkFlow" }

    private fun ProducerScope<Boolean>.createNetworkCallback() =
        object : ConnectivityManager.NetworkCallback() {
            private var availableNetworks = 0
            override fun onAvailable(network: Network) {
                val networkCapabilities = cm.getNetworkCapabilities(network)
                val hasInternet = networkCapabilities?.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_INTERNET
                )
                if (hasInternet == true) {
                    availableNetworks++
                    Timber.tag(TAG).d("availableNetworks: $availableNetworks")
                    trySend(isNetworkAvailable())
                }
            }

            override fun onLost(network: Network) {
                availableNetworks--
                Timber.tag(TAG).d("availableNetworks: $availableNetworks")
                trySend(isNetworkAvailable())
            }

            private fun isNetworkAvailable() = availableNetworks > 0
        }

    override suspend fun collect(collector: FlowCollector<Boolean>) {
        _hasNetwork.collect(collector)
    }


}