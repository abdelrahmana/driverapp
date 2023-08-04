package com.tt.driver.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentSender
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LocationHelper(private val context: Context) {

    private var fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val locationRequest = LocationRequest.create().apply {
        interval = 6000
        fastestInterval = 5000
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    fun checkIfAllLocationProvidersAndPermissionsSatisfied(
        launcher: ActivityResultLauncher<IntentSenderRequest>,
        satisfied: () -> Unit
    ) {
        LocationServices.getSettingsClient(context)
            .checkLocationSettings(
                LocationSettingsRequest
                    .Builder()
                    .addLocationRequest(locationRequest)
                    .build()
            )
            .addOnSuccessListener {
                satisfied()
            }
            .addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    try {
                        val intentSenderRequest =
                            IntentSenderRequest.Builder(exception.resolution).build()
                        launcher.launch(intentSenderRequest)
                    } catch (sendEx: IntentSender.SendIntentException) {
                    }
                }
            }
    }

    fun getUserLocation(
        coroutineScope: CoroutineScope,
        onError: () -> Unit,
        onLocationAvailable: (Location) -> Unit
    ) {
        coroutineScope.launch(Dispatchers.Main) {
            requestLastKnownLocation()?.let { onLocationAvailable(it) }
            requestCurrentLocation()?.let {
                onLocationAvailable(it)
            } ?: requestLastKnownLocation()?.let {
                onLocationAvailable(it)
            } ?: onError()
        }
    }

    @SuppressLint("MissingPermission")  //call this method only when location permission is granted
    private suspend fun requestLastKnownLocation() = fusedLocationClient.lastLocation.await()

    @SuppressLint("MissingPermission")
    private suspend fun requestCurrentLocation(): Location? {
        return fusedLocationClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            null
        ).await()
    }

    @SuppressLint("MissingPermission")
    fun requestLocationUpdates(callback: LocationCallback) {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            callback,
            Looper.getMainLooper()
        ).addOnSuccessListener {
            Log.e("LocationObserver", "observing user location..")
        }.addOnFailureListener {
            Log.e("LocationObserver", "Failure: ${it.message}")
        }
    }

}