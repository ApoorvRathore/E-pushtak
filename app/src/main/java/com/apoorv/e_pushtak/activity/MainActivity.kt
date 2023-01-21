@file:Suppress("DEPRECATION")

package com.apoorv.e_pushtak.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.apoorv.e_pushtak.*
import com.apoorv.e_pushtak.fragments.AboutFragment
import com.apoorv.e_pushtak.fragments.DashboardFragment
import com.apoorv.e_pushtak.fragments.FavouriteFragment
import com.apoorv.e_pushtak.fragments.ProfileFragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var coordinatorLayout: CoordinatorLayout
    private lateinit var toolbar: Toolbar
    private lateinit var frameLayout: FrameLayout
    private lateinit var navigationView: NavigationView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var user: FirebaseUser
    private var previousMenuItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        setContentView(R.layout.activity_main)
        drawerLayout = findViewById(R.id.drawerlayout)
        coordinatorLayout = findViewById(R.id.coordinatorlayout)
        toolbar = findViewById(R.id.Toolbar)
        frameLayout = findViewById(R.id.frame)
        navigationView = findViewById(R.id.navigationview)
        setUpToolbar()
        openDashboard()

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity, drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        navigationView.setNavigationItemSelectedListener {

            if (previousMenuItem != null) {
                previousMenuItem?.isChecked = false
            }
            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it

            when (it.itemId) {
                R.id.dashboard -> {
                    openDashboard()
                    drawerLayout.closeDrawers()
                }
                R.id.profile -> {
                    if (user != null) {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.frame, ProfileFragment())
                            .commit()
                        drawerLayout.closeDrawers()
                        supportActionBar?.title = "Profile"
                    } else {
                        val intent = Intent(this@MainActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                }
                R.id.favourite -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, FavouriteFragment())
                        .commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title = "Favourite"
                }
                R.id.about -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, AboutFragment())
                        .commit()
                    drawerLayout.closeDrawers()
                    supportActionBar?.title = "About"

                }
                R.id.logout -> {

                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    deletePreference()
                    Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                }
            }
            return@setNavigationItemSelectedListener true

        }
    }

    override fun onStop() {
        super.onStop()
        finish()
    }

    private fun deletePreference() {
        sharedPreferences.edit().remove("Is Logged In").apply()
    }

    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "E-Pushtak"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openDashboard() {
        val fragment = DashboardFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, fragment)
        transaction.commit()
        supportActionBar?.title = "Dashboard"
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        when (supportFragmentManager.findFragmentById(R.id.frame)) {
            !is DashboardFragment -> openDashboard()
            else -> super.onBackPressed()
        }
        navigationView.setCheckedItem(R.id.dashboard)
    }

    override fun onStart() {
        super.onStart()
        user = FirebaseAuth.getInstance().currentUser!!
    }
}

