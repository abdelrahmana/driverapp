package com.tt.driver.data.services

import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.IBinder
import android.util.Log
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.tt.driver.data.datastore.UserDataStore
import com.tt.driver.data.managers.NotificationHelper
import com.tt.driver.data.repositories.auth.AuthRepository
import com.tt.driver.utils.LocationHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@AndroidEntryPoint
class LocationTrackerService : Service() {

    private var currentLocation: Location? = null

    private val job = Job()

    @Inject
    lateinit var repository: AuthRepository

    @Inject
    lateinit var userDataStore: UserDataStore

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        startForegroundNotification()

        LocationHelper(this@LocationTrackerService).requestLocationUpdates(object :
            LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                p0.lastLocation?.let {
                    Log.d("com.diver.location: ", "${it.latitude}, ${it.longitude}")
                    currentLocation = it
                }
            }
        })

        CoroutineScope(Dispatchers.IO + job).launch {
            while (true) {
                currentLocation?.let {
                    if (userDataStore.getUser().first()?.isOnline() == true)
                        repository.emitUserLocation(it.latitude, it.longitude)
                }
                delay(30000)
            }
        }

        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        stopForegroundNotification()
    }

    override fun onBind(p0: Intent?): IBinder? {
//        stopForegroundNotification()
        return null
    }

//    override fun onUnbind(intent: Intent?): Boolean {
//        startForegroundNotification()
//        return super.onUnbind(intent)
//    }
//
//    override fun onRebind(intent: Intent?) {
//        stopForegroundNotification()
//    super.onRebind(intent)
//    }

    private fun startForegroundNotification() {
        val notification = NotificationHelper.generateLocationOnGoingNotification(applicationContext)
        startForeground(10, notification)
    }

    private fun stopForegroundNotification() {
        stopForeground(true)
    }
}