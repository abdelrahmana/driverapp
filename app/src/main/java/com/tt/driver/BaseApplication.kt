package com.tt.driver

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.libraries.places.api.Places
import com.google.firebase.messaging.FirebaseMessaging
import com.tt.driver.data.datastore.AuthDataStore
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Inject

@HiltAndroidApp
class BaseApplication : Application() {

    @Inject
    lateinit var authDataStore: AuthDataStore

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        Places.initialize(applicationContext, getString(R.string.google_map_key), Locale.US)
        try {
            registerNotificationToken()
        } catch (ex: Exception) {
        }
    }

    private fun registerNotificationToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("FB_TOKEN", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            val currentToken = runBlocking { authDataStore.getFCMToken().first() }
            if (token != currentToken)
                authDataStore.changeAuthStatus(GlobalScope, false)
            authDataStore.setFCMToken(GlobalScope, token)
            Log.e("TOKEN", token)
        })
    }

}