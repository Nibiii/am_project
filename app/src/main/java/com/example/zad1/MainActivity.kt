package com.example.zad1

import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var  actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        drawerLayout = findViewById(R.id.myDrawer_layout)
        navView = findViewById(R.id.navView)
        // init action bar drawer toggle
        actionBarDrawerToggle = ActionBarDrawerToggle(this,drawerLayout,R.string.nav_open,R.string.nav_close)
        // add a drawer listener into  drawer layout
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        // show menu icon and back icon while drawer open
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        sharedPreferences = getSharedPreferences("settings", MODE_PRIVATE)

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_theme -> {
                    toggleTheme()
                    true
                }
                else -> false
            }.also {
                drawerLayout.closeDrawer(GravityCompat.START)
            }
        }

        applySavedTheme()
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        // check conndition for drawer item with menu item
//        return if (actionBarDrawerToggle.onOptionsItemSelected(item)){
//            true
//        }else{
//            super.onOptionsItemSelected(item)
//        }

//    }

    private fun toggleTheme() {
        val currentMode = sharedPreferences.getInt("theme", AppCompatDelegate.MODE_NIGHT_NO)
        val newMode = if (currentMode == AppCompatDelegate.MODE_NIGHT_NO) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(newMode)
        with(sharedPreferences.edit()) {
            putInt("theme", newMode)
            apply()
        }
    }

    private fun applySavedTheme() {
        val savedMode = sharedPreferences.getInt("theme", AppCompatDelegate.MODE_NIGHT_NO)
        AppCompatDelegate.setDefaultNightMode(savedMode)
    }

    override fun onSupportNavigateUp(): Boolean {
        drawerLayout.openDrawer(GravityCompat.START)
        return true
    }
}