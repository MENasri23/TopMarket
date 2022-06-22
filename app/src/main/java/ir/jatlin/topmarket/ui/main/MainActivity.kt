package ir.jatlin.topmarket.ui.main

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.work.WorkInfo
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.databinding.ActivityMainBinding
import ir.jatlin.topmarket.service.sync.FetchNewestProductsWorker
import ir.jatlin.topmarket.ui.loading.LoadStateViewModel
import ir.jatlin.topmarket.ui.theme.ThemeViewModel
import ir.jatlin.topmarket.ui.util.updateTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()
    private val loadingViewModel by viewModels<LoadStateViewModel>()
    private val themeViewModel by viewModels<ThemeViewModel>()

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    private val topLevelDestinations = arrayOf(
        R.id.homeFragment,
        R.id.productCategoryFragment,
        R.id.purchaseFragment,
        R.id.mapFragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        updateTheme(themeViewModel.currentTheme)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        }

        val navHost = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHost.navController

        setupBottomNavMenu(navController)

        collectUiStates()

        tryEnqueueNewestProductsWork()

    }

    private fun setupBottomNavMenu(navController: NavController) {
        binding.bottomNav.apply {
            setupWithNavController(navController)
            setOnItemSelectedListener {
                NavigationUI.onNavDestinationSelected(
                    it, navController
                )
                true
            }
            navController.addOnDestinationChangedListener { _, destination, _ ->
                isVisible = destination.id in topLevelDestinations
            }
        }
    }

    private fun collectUiStates() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    /* Show loading screen based on the current state of each visible fragment */
                    loadingViewModel.loading.collectLatest { isLoading ->
                        binding.loadingScreen.isVisible = isLoading
                    }
                }

                launch {
                    themeViewModel.selectedTheme.collectLatest {
                        Timber.d("$theme")
                        updateTheme(it)
                    }
                }

                launch {
                    viewModel.isNetworkAvailable.collect { isOnline ->
                        if (!isOnline) {
                            navigateToNoNetworkConnection()
                        } else {
                            tryPopUpNoNetworkConnection()
                        }
                    }
                }

            }


        }

    }

    private fun tryPopUpNoNetworkConnection() {
        val topBackStackEntry = navController.backQueue.lastOrNull()
        val destinationId = topBackStackEntry?.destination?.id ?: return

        if (destinationId == R.id.noNetworkConnectionFragment) {
            navController.popBackStack()
        }
    }

    private fun navigateToNoNetworkConnection() {
        navController.navigate(R.id.noNetworkConnectionFragment)
    }

    private fun tryEnqueueNewestProductsWork() {
        val workInfosLiveData = WorkManager.getInstance(this)
            .getWorkInfosForUniqueWorkLiveData(FetchNewestProductsWorker.WORKER_NAME)

        val observer = object : Observer<MutableList<WorkInfo>> {

            override fun onChanged(t: MutableList<WorkInfo>?) {
                if (t.isNullOrEmpty()) {
                    Timber.d("work info is empty or null")
                    viewModel.enqueueNewestProductsWorkRequest()
                    return
                }
                val workInfoState = t[0].state
                Timber.d("$workInfoState")
                /*
                if (workInfoState != WorkInfo.State.ENQUEUED &&
                    workInfoState != WorkInfo.State.RUNNING
                ) {
                    viewModel.enqueueNewestProductsWorkRequest()
                }*/

                // TODO: Remove observer
//                workInfosLiveData.removeObserver(this)
            }
        }

        workInfosLiveData.observeForever(observer)
    }

}