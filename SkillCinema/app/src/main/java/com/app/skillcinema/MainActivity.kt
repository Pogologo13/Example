package com.app.skillcinema


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.app.skillcinema.databinding.ActivityMainBinding
import com.app.skillcinema.utils.BottomBarHandle
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), BottomBarHandle {
    private var searchDestination: Bundle? = null

    private val navView: BottomNavigationView by lazy { findViewById(R.id.nav_view) }

    private lateinit var binding: ActivityMainBinding
    private var safeArgs: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController: NavController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    if (navController.currentDestination?.id == R.id.navigation_home) {
                        navController.navigateUp()
                    } else {
                        navController.navigate(R.id.navigation_home)
                    }
                    return@setOnItemSelectedListener true
                }

                R.id.navigation_search -> {
                    if (navController.currentDestination?.id == R.id.navigation_search) {
                        navController.navigateUp()
                    } else {
                        navController.navigate(R.id.navigation_search, safeArgs)
                    }
                    return@setOnItemSelectedListener true
                }

                R.id.navigation_profile -> {
                    if (navController.currentDestination?.id == R.id.navigation_profile) {
                        navController.navigateUp()
                    } else {
                        navController.navigate(R.id.navigation_profile, searchDestination)
                    }
                    return@setOnItemSelectedListener true
                }

                else -> return@setOnItemSelectedListener false
            }
        }
    }

    override fun uncheckAllItemsInBottomMenu() {
        navView.menu.setGroupCheckable(0, true, false)
        navView.visibility = View.VISIBLE
        navView.menu.forEach {
            it.isChecked = false
        }
        navView.menu.setGroupCheckable(0, true, true)
    }

    override fun getArguments(args: Bundle) {
        safeArgs = args
    }

    override fun showBottomBar() {
        navView.visibility = View.VISIBLE
    }

    override fun hideBottomBar() {
        navView.visibility = View.GONE
    }
}