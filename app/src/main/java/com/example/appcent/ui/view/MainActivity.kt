package com.example.appcent.ui.view

import android.R.id
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.appcent.R
import com.example.appcent.util.Util
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    lateinit var navController: NavController
    lateinit var bottomNav: BottomNavigationView

    private val bottomNavViews = arrayOf(
        R.id.favoriteGamesFragment,
        R.id.gamesFragment,
    )

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupNavigation()
        firebaseAnalytics = Firebase.analytics
        Util.launchEvent(firebaseAnalytics)
        initFcm()
    }

    fun initFcm() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }

            val token = task.result
        })
    }

    /**
     * Setting up navigation with toolbar and top level fragments
     */
    private fun setupNavigation() {
        bottomNav = findViewById(R.id.bottom_navigation)
        bottomNav.setOnNavigationItemReselectedListener {
            // Empty Block -> Do not write any code here
        }
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        val appBarConfiguration =
            AppBarConfiguration.Builder(R.id.gamesFragment, R.id.favoriteGamesFragment).build()
        NavigationUI.setupWithNavController(bottomNav, navController)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)

        navController.addOnDestinationChangedListener(this)

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }

    private fun showBottomNav(shouldShow: Boolean) {
        bottomNav.visibility = if (shouldShow) View.VISIBLE else View.GONE
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        if (bottomNavViews.contains(destination.id)) {
            showBottomNav(true)
        } else {
            showBottomNav(false)
        }
    }
}