package com.tt.driver.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.tt.driver.R
import com.tt.driver.data.datastore.AuthDataStore
import com.tt.driver.ui.components.main.MainActivity
import com.tt.driver.ui.components.registration.RegistrationActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var authDataStore: AuthDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Glide.with(this).load(R.drawable.appbg).into(findViewById(R.id.background))

        lifecycleScope.launch(Dispatchers.IO) {
            delay(2000)
            if (authDataStore.isUserLoggedIn().first())
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            else startActivity(Intent(this@SplashActivity, RegistrationActivity::class.java))
            finish()
        }

    }

}