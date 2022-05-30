package ir.jatlin.topmarket.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
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
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val loadingViewModel by viewModels<LoadSateViewModel>()

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    private val topLevelDestinations = arrayOf(
        R.id.homeFragment,
        R.id.productCategoryFragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost = supportFragmentManager
            .findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHost.navController
        setupBottomNavMenu(navController)

        navController.addOnDestinationChangedListener { navController, destination, _ ->
            val bottomNav = binding.bottomNav
            bottomNav.isVisible = destination.id in topLevelDestinations
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    /* Show loading screen based on the current state of each visible fragment */
                    loadingViewModel.loading.collect { isLoading ->
                        binding.loadingScreen.isVisible = isLoading
                    }
                }
            }
        }

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
        }
    }

}