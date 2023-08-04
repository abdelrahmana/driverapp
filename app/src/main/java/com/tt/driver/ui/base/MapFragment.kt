package com.tt.driver.ui.base

import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.tt.driver.utils.MapHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class MapFragment<T : ViewBinding> : LocationAwareFragment<T>() {

    protected var map: GoogleMap? = null

    private var userLocationMarker: Marker? = null

    private var searchedLocationMarker: Marker? = null

    protected fun moveCameraPosition(location: LatLng, animated: Boolean = true) {
        map?.let {
            val cameraPosition = CameraPosition.builder().target(location).zoom(15f).build()
            it.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            if (animated)
                animateCameraPosition(cameraPosition)
        }
    }

    private fun animateCameraPosition(cameraPosition: CameraPosition) {
        map?.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    protected fun pinUserLocation(location: LatLng, image: String? = null) {
        lifecycleScope.launch(Dispatchers.IO) {
            MapHelper.getPinIconFromUrl(image, requireContext()) {
                launch(Dispatchers.Main) {
                    userLocationMarker?.remove()
                    userLocationMarker = map?.addMarker(
                        MarkerOptions()
                            .position(location)
                            .apply {
                                it?.let { icon(it) }
                            }
                    )
                }
            }
        }
    }

    protected fun pinSearchedLocation(location: LatLng) {
        searchedLocationMarker?.remove()
        searchedLocationMarker = map?.addMarker(MarkerOptions().position(location))
        moveCameraPosition(location, true)
    }

    protected fun clearSearchedLocationPin() {
        searchedLocationMarker?.remove()
        userLocationMarker?.let {
            moveCameraPosition(it.position, true)
        }
    }

}