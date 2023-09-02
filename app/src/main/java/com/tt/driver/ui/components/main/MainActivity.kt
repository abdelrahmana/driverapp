package com.tt.driver.ui.components.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
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
import com.google.android.material.snackbar.Snackbar
import com.tt.driver.data.datastore.AuthDataStore
import com.tt.driver.data.datastore.UserDataStore
import com.tt.driver.data.models.Failure
import com.tt.driver.data.models.Success
import com.tt.driver.data.services.LocationTrackerService
import com.tt.driver.ui.components.registration.RegistrationActivity
import com.tt.driver.utils.IntentUtils
import com.waysgroup.t7t_talbk_driver.R
import com.waysgroup.t7t_talbk_driver.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainActivityViewModel>()

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var authDataStore: AuthDataStore

    @Inject
    lateinit var userDataStore: UserDataStore

    private var isLocationTrackingServiceStarted = false

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestNotificationPermission()

        binding.navView.setupWithNavController(getNavController())

        binding.navView.menu[4].setOnMenuItemClickListener {
            IntentUtils.dialPhone(this, "94129624")
            true
        }

        binding.navView.menu[5].setOnMenuItemClickListener {
            fetchHelpContact()
            true
        }

        binding.logoutButton.setOnClickListener {
            logout()
        }

        updateDrawerUserInfo()
    }

    private fun logout() {
        authDataStore.changeAuthStatus(lifecycleScope, false)
        startActivity(Intent(this, RegistrationActivity::class.java))
        finish()
    }

    fun openDrawer() {
        binding.drawer.open()
    }

    private fun getNavController() =
        (supportFragmentManager.findFragmentById(R.id.mainHostFragment) as NavHostFragment).findNavController()

    private fun updateDrawerUserInfo() {
        lifecycleScope.launch {
            userDataStore.getUser().collect {
                with(binding.navView.getHeaderView(0)) {
                    findViewById<TextView>(R.id.name).text = it?.name
                    findViewById<ImageView>(R.id.image).apply {
                        Glide.with(this).load(it?.image).into(this)
                    }
                    findViewById<ImageView>(R.id.menuIcon).setOnClickListener {
                        binding.drawer.close()
                    }

                }
            }
        }
    }

    private fun fetchHelpContact() {
        viewModel.getHelpContact().observe(this) {
            when (it) {
                is Success -> {
                    IntentUtils.dialPhone(this@MainActivity, it.data)
                }
                is Failure -> {
                    Toast.makeText(
                        this@MainActivity,
                        "something went wrong",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
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