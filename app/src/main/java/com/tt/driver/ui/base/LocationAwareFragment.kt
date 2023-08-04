package com.tt.driver.ui.base

import android.Manifest
import android.app.Activity
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.fondesa.kpermissions.allGranted
import com.fondesa.kpermissions.extension.permissionsBuilder
import com.fondesa.kpermissions.extension.send
import com.tt.driver.utils.LocationHelper
import com.tt.driver.utils.showToast

abstract class LocationAwareFragment<T : ViewBinding> : BaseFragment<T>() {

    private val locationHelper by lazy { LocationHelper(requireActivity()) }

    private lateinit var launcher: ActivityResultLauncher<IntentSenderRequest>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        preLocationPermissionRequest()
        requestLocationPermissionsAndFetchUserLocation()
    }

    private fun preLocationPermissionRequest() {
        //handle settings client intent in case location provider in turned off
        launcher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                onLocationPermissionsSatisfied()
            }
        }
    }

    private fun requestLocationPermissionsAndFetchUserLocation() {
        permissionsBuilder(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ).build().send { result ->
            if (result.allGranted()) {
                locationHelper.checkIfAllLocationProvidersAndPermissionsSatisfied(launcher) {
                    onLocationPermissionsSatisfied()
                }
            }
        }
    }

    open fun onLocationPermissionsSatisfied() {
        fetchUserCurrentLocation()
    }

    private fun fetchUserCurrentLocation() {
        locationHelper.getUserLocation(
            lifecycleScope,
            { showToast("device's location is not available") },
            {
                onUserLocationFetched(it)
            }
        )
    }

    abstract fun onUserLocationFetched(location: Location)

}