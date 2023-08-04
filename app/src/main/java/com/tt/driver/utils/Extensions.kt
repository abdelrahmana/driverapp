package com.tt.driver.utils

import android.location.Location
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.model.LatLng

fun View.show(visible: Boolean = true) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun EditText.value() = text.toString().trim()

fun Fragment.showToast(message: String?) {
    Toast.makeText(requireContext(), message?:"", Toast.LENGTH_SHORT).show()
}

fun Location.toLatLng() = LatLng(latitude, longitude)

fun LatLng.toLocation() = Location("").apply {
    latitude = this@toLocation.latitude
    longitude = this@toLocation.longitude
}