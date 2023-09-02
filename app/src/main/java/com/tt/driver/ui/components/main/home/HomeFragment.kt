package com.tt.driver.ui.components.main.home

import android.content.ContentValues.TAG
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.common.api.Status
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.tt.driver.data.datastore.UserDataStore
import com.tt.driver.ui.base.MapFragment
import com.tt.driver.ui.components.main.MainActivity
import com.tt.driver.utils.toLatLng
import com.waysgroup.t7t_talbk_driver.R
import com.waysgroup.t7t_talbk_driver.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : MapFragment<FragmentHomeBinding>() {

    val viewModel by viewModels<HomeViewModel>()

    @Inject
    lateinit var userDataStore: UserDataStore

    private var moveCameraToUserLocation = true

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentHomeBinding.inflate(inflater, container, false)

    override fun onUserLocationFetched(location: Location) {
        pinUserImageOnMap(location)
        if (moveCameraToUserLocation) {
            moveCameraPosition(location.toLatLng())
            moveCameraToUserLocation = false
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment?
        mapFragment?.getMapAsync {
            map = it
        }

        binding {

            menuIcon.setOnClickListener {
                (requireActivity() as? MainActivity)?.openDrawer()
            }

            statusSwitch.setOnClickListener {
                val checked = statusSwitch.isChecked
                statusSwitch.isEnabled = false
                observeResult(
                    viewModel.toggleStatus(checked),
                    customErrorHandling = {
                        statusSwitch.isEnabled = true
                        updateStatus(!checked)
                    }, successResult = {
                        statusSwitch.isEnabled = true
                        updateStatus(checked)
                    }
                )
            }

            lifecycleScope.launchWhenResumed {
                updateStatus(userDataStore.getUser().first()?.isOnline() ?: false)
            }

        }

        initAutoCompleteSearch()

    }

    private fun updateStatus(isOnline: Boolean) {
        binding {
            statusLabel.text = if (isOnline) "Online" else "Offline"
            statusSwitch.isChecked = isOnline
            userDataStore.updateOnlineStatus(lifecycleScope, isOnline)
        }
    }

    private fun pinUserImageOnMap(location: Location) {
        lifecycleScope.launch {
            val userImage = userDataStore.getUser().first()?.image
            pinUserLocation(location.toLatLng(), userImage)
        }
    }

    override fun onLocationPermissionsSatisfied() {
        super.onLocationPermissionsSatisfied()
        (requireActivity() as? MainActivity)?.startLocationTrackingService()
    }

    private fun initAutoCompleteSearch() {

        val autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment

        autocompleteFragment.setPlaceFields(listOf(Place.Field.LAT_LNG, Place.Field.NAME))

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                place.latLng?.let { pinSearchedLocation(it) }
            }

            override fun onError(status: Status) {
                Log.e(TAG, "An error occurred: $status")
            }
        })

        autocompleteFragment.view
            ?.findViewById<View>(com.google.android.libraries.places.R.id.places_autocomplete_clear_button)
            ?.setOnClickListener { view ->
                autocompleteFragment.setText("")
                view.visibility = View.GONE
                clearSearchedLocationPin()
            }

    }
}