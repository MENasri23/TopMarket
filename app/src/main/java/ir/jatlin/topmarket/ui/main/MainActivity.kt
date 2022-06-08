package ir.jatlin.topmarket.ui.main

import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import ir.jatlin.topmarket.R
import ir.jatlin.topmarket.databinding.ActivityMainBinding
import ir.jatlin.topmarket.ui.loading.LoadSateViewModel
import ir.jatlin.topmarket.ui.theme.ThemeViewModel
import ir.jatlin.topmarket.ui.util.updateTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()
    private val loadingViewModel by viewModels<LoadSateViewModel>()
    private val themeViewModel by viewModels<ThemeViewModel>()

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    private val topLevelDestinations = arrayOf(
        R.id.homeFragment,
        R.id.productCategoryFragment
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
            }
        }

    }

}