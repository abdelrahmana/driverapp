package com.tt.driver.ui.components.main.orders.order_details

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.model.LatLng
import com.tt.driver.R
import com.tt.driver.data.models.entities.Order
import com.tt.driver.data.models.entities.OrderStatus
import com.tt.driver.data.models.http.OrderDetailsResponse
import com.tt.driver.databinding.FragmentOrderDetailsBinding
import com.tt.driver.ui.base.LocationAwareFragment
import com.tt.driver.ui.components.main.orders.order_utils.OrderCallActionsWrapper
import com.tt.driver.utils.IntentUtils
import com.tt.driver.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderDetailsFragment : LocationAwareFragment<FragmentOrderDetailsBinding>() {

    private val viewModel by viewModels<OrderViewModel>()

    private var currentLocation: Location? = null

    private var nextOrderState: OrderStatus? = null;

    companion object {
        const val UPDATE_ORDER_STATE = "UPDATE_ORDER_STATE"
    }

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentOrderDetailsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeResult(viewModel.order) {
            binding?.progressBar?.show(false)
            updateUI(it)
        }

        viewModel.showDigitalSignature.observe(viewLifecycleOwner) {
            it?.let { navigateToDigitalSignatureScreen(it) }
        }

        //update order when user navigates back from payment
        findNavController()
            .currentBackStackEntry
            ?.savedStateHandle
            ?.apply {
                getLiveData<Boolean>(UPDATE_ORDER_STATE)
                    .observe(viewLifecycleOwner) {
                        it?.let {
                            if (it)
                                viewModel.updateOrderStatus(nextOrderState ?: return@observe)
                            else viewModel.refreshOrderDetails()
                            this.set(UPDATE_ORDER_STATE, null)  //reset value after updating
                        }
                    }
            }

    }

    private fun updateUI(orderDetailsResponse: OrderDetailsResponse) {
        val order = orderDetailsResponse.data ?: return

        binding?.run {

            toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }

            container.show()

            card.order = order

            card.callActions = OrderCallActionsWrapper(requireContext(), card.dropOffCallButton)

            order.notes?.let { notes ->
                card.alert.show()
                card.alertMessage.text = notes
            }

            val currentDestination = order.getHeadedLocation()

            binding?.calculatedDistance?.text = order.status_word

            if (order.isActive())
                setDistanceInKM(currentDestination, order.status_word)

            binding?.goThereButton?.setOnClickListener {
                IntentUtils.launchGoogleMapAndMarkLocation(
                    requireContext(),
                    currentDestination.latitude,
                    currentDestination.longitude
                )
            }

            updateStatusButton(order)
        }

    }

    private fun setDistanceInKM(current: LatLng, status: String?) {
        with(FloatArray(1)) {
            Location.distanceBetween(
                currentLocation?.latitude ?: 0.0,
                currentLocation?.longitude ?: 0.0,
                current.latitude,
                current.longitude,
                this
            )
            binding?.calculatedDistance?.text = getString(
                R.string.order_calculated_distance,
                this[0].div(1000),
                status,
            )
        }
    }

    private fun updateStatusButton(order: Order) {
        when (order.getStatus()) {
            OrderStatus.PENDING -> {
                binding?.updateStatusButton?.text = "Pick Up"
                nextOrderState = OrderStatus.PICK_UP
                binding?.updateStatusButton?.setOnClickListener {
                    navigateTo(
                        OrderDetailsFragmentDirections.actionOrderDetailsFragmentToOrderDestinationFragment(
                            order
                        )
                    )
                }
            }
            OrderStatus.PICK_UP -> {
                binding?.updateStatusButton?.text = "On The Way"
                binding?.updateStatusButton?.setOnClickListener {
                    viewModel.updateOrderStatus(OrderStatus.ON_THE_WAY)
                }
            }
            OrderStatus.ON_THE_WAY -> {
                binding?.updateStatusButton?.text = "Complete"
                nextOrderState = OrderStatus.COMPLETED
                binding?.updateStatusButton?.setOnClickListener {
                    navigateTo(
                        OrderDetailsFragmentDirections.actionOrderDetailsFragmentToOrderDestinationFragment(
                            order
                        )
                    )
                }
            }
            else -> {
                binding?.updateStatusButton?.visibility = View.GONE
                binding?.card?.pickUpCallButton?.show(false)
                binding?.card?.dropOffCallButton?.show(false)
                binding?.goThereButton?.show(false)
            }
        }
    }

    override fun onUserLocationFetched(location: Location) {
        currentLocation = location
    }

    override fun isLoading(status: Boolean) {
        binding {
            progressBar.show(status)
            container.show(!status)
        }
    }

    private fun navigateToDigitalSignatureScreen(orderId: Int) {
        navigateTo(
            OrderDetailsFragmentDirections.actionOrderDetailsFragmentToDigitalSignatureFragment(
                orderId
            )
        )
    }

}