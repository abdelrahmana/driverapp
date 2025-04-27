package com.tt.driver.ui.components.main

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.tt.driver.data.datastore.AuthDataStore
import com.tt.driver.data.datastore.UserDataStore
import com.tt.driver.data.models.Failure
import com.tt.driver.data.models.Success
import com.tt.driver.data.services.LocationTrackerService
import com.tt.driver.ui.components.registration.RegistrationActivity
import com.tt.driver.utils.Constant
import com.tt.driver.utils.IntentUtils
import com.tt.driver.utils.Util
import com.waysgroup.speed.R
import com.waysgroup.speed.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var authDataStore: AuthDataStore
    @Inject lateinit var prefs : SharedPreferences

    @Inject
    lateinit var userDataStore: UserDataStore

    private var isLocationTrackingServiceStarted = false

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager
            .findFragmentById(binding.mainHostFragment.id) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)
        requestNotificationPermission()
    }

 /*   private fun logout() {
        authDataStore.changeAuthStatus(lifecycleScope, false)
        startActivity(Intent(this, RegistrationActivity::class.java))
        finish()
    }*/


    private fun getNavController() =
        (supportFragmentManager.findFragmentById(R.id.mainHostFragment) as NavHostFragment).findNavController()

    override fun onResume() {
        Util.setLanguagePerActivity(this,null,prefs)
        //  UtilKotlin.setLocalLanguage(UtilKotlin.getSharedPrefs(this).getString(PrefsModel.localLanguage,"ar"))

        super.onResume()
    }

    fun startLocationTrackingService() {
        if (!isLocationTrackingServiceStarted) {
            isLocationTrackingServiceStarted = true
            startService(
                Intent(
                    this,
                    LocationTrackerService::class.java
                )
            )
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                Snackbar.make(
                    binding.root,
                    "Notification permission is denied. please enable it from settings",
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

}