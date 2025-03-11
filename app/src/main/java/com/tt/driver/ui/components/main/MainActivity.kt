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

    private val viewModel by viewModels<MainActivityViewModel>()

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

        requestNotificationPermission()

        binding.navView.setupWithNavController(getNavController())

        binding.navView.menu[2].setOnMenuItemClickListener {
            IntentUtils.dialPhone(this, "94129624")
            true
        }

        binding.navView.menu[3].setOnMenuItemClickListener {
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
    override fun onResume() {
        Util.setLanguagePerActivity(this,null,prefs)
        //  UtilKotlin.setLocalLanguage(UtilKotlin.getSharedPrefs(this).getString(PrefsModel.localLanguage,"ar"))
        updateDrawerMenu(binding.navView, this)

        super.onResume()
    }
    fun updateDrawerMenu(navView: NavigationView, context: Context) {
//        navView.menu.findItem(R.id.homeFragment).title = context.getString(R.string.home_menu_item)
        navView.menu.findItem(R.id.runSheetFragment).title = context.getString(R.string.run_sheet)
        navView.menu.findItem(R.id.languageFragment).title = context.getString(R.string.language)
        navView.menu.findItem(R.id.contactUs).title = context.getString(R.string.contact_us_menu_item)
        navView.menu.findItem(R.id.help).title = context.getString(R.string.help_menu_item)
        binding.logoutButton.text = getString(R.string.log_out)

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