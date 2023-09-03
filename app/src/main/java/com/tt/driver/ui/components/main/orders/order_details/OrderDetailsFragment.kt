package com.tt.driver.ui.components.main.orders.order_details

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.model.LatLng
import com.tt.driver.data.models.entities.Order
import com.tt.driver.data.models.entities.OrderStatus
import com.tt.driver.data.models.http.OrderDetailsResponse
import com.tt.driver.ui.base.LocationAwareFragment
import com.tt.driver.utils.IntentUtils
import com.tt.driver.utils.Util
import com.tt.driver.utils.show
import com.waysgroup.t7t_talbk_driver.R
import com.waysgroup.t7t_talbk_driver.databinding.FragmentOrderDetailsNewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderDetailsFragment : LocationAwareFragment<FragmentOrderDetailsNewBinding>() {

    private val viewModel by viewModels<OrderViewModel>()

    private var currentLocation: Location? = null

    private var nextOrderState: OrderStatus? = null;

    companion object {
        const val UPDATE_ORDER_STATE = "UPDATE_ORDER_STATE"
    }
    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentOrderDetailsNewBinding.inflate(inflater, container, false)

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
                getLiveData<Boolean>(UPDATE_ORDER_STATE) // listen of observer value
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

            toolbar.setOnClickListener { requireActivity().onBackPressed() }

            container.show()
            if (order.to_address.isNullOrEmpty())
                containerDestination.visibility = View.GONE
            dateTextPurpose.text = order.valid_date + " | " + order.order_type
            orderType.text = order.delivery_category?.name?:""

            nameCustomer.text = order.to_name?:""
            goveranteValuePickUp.text = order.from_governorate?.name?:""
            regionValuePickup.text = order.from_region?.name?:""
            blockValuePickUp.text = order.from_block?.name?:""
            homePickUpValue.text = order.to_home_number?:""
            apartmentPickUp.text = order.to_apartment_number?:""
            floorNumberValue.text = order.to_floor_number?:""


            nameCustomerDestionationValue.text = order.to_name?:""
            goveranteValueDestionation.text = order.to_governorate?.name?:""
            regionValueDestionation.text = order.to_region?.name?:""
            blockValueDestionation.text = order.to_region?.name?:""
            homeDestionationValue.text = order.to_home_number?:""
            apartmentDestionationValue.text = order.to_apartment_number?:""
            floorDestionationValue.text = order.to_floor_number?:""

            priceValue.text = order.total_order_price?:""
            amountHeader.text = if (order.paid =="1") getString(R.string.amount_new) else getString(R.string.amount)
            callDestination.setOnClickListener{
                val url = "https://api.whatsapp.com/send?phone="+"+965"+order.from_phone?:"0"
                Util.buildImplictIntentView(requireContext(), url)
            }
            callPickUp.setOnClickListener{
                Util.startIntentAction("+965"+order?.from_phone,requireContext()!!,
                    requireActivity()!!.getString(R.string.call), Intent.ACTION_DIAL)
            }

            whatsDestionationValue.setOnClickListener{
                val url = "https://api.whatsapp.com/send?phone="+"+965"+order.to_phone?:"0"
                Util.buildImplictIntentView(requireContext(), url)
            }
            callDestionationValue.setOnClickListener{
                Util.startIntentAction("+965"+order?.to_phone,requireContext()!!,
                    requireActivity()!!.getString(R.string.call), Intent.ACTION_DIAL)
            }
            //     card.order = order

      //      card.callActions = OrderCallActionsWrapper(requireContext(), card.dropOffCallButton)

        /*    order.notes?.let { notes ->
                card.alert.show()
                card.alertMessage.text = notes
            }*/

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
        when (order.getStatus()) { // the current order status
            OrderStatus.PENDING -> { // pending
                binding?.updateStatusButton?.text = "Pick Up"
                nextOrderState = OrderStatus.PICK_UP
                binding?.updateStatusButton?.setOnClickListener {
                    //actionOrderDetailsFragmentToOrderDestinationFragment
                    navigateTo(
                        OrderDetailsFragmentDirections.actionOrderDetailsPickup( // to pickup reached
                            order
                        )
                    )
                }
            }
            OrderStatus.PICK_UP -> { // pickup reached
                if (order.to_address.isNullOrEmpty()) // doesn't have destination
                    goToDestinationReached(OrderStatus.COMPLETED,getString(R.string.status_compelete),order)
                else { // start the destination
                    /*
                    binding?.updateStatusButton?.text = "On The Way"
                    binding?.updateStatusButton?.setOnClickListener {
                        viewModel.updateOrderStatus(OrderStatus.ON_THE_WAY)
                    }*/
                    // go to destination prepration first
                    navigateTo( // has destionation
                        OrderDetailsFragmentDirections.actionDestinationPreview( // to pickup reached
                            order
                        ))

                }
            }
            OrderStatus.ON_THE_WAY -> {
                goToDestinationReached(OrderStatus.COMPLETED,getString(R.string.status_compelete),order)
            }
            else -> { // in case of order completed
                binding?.updateStatusButton?.visibility = View.GONE
              //  binding?.card?.pickUpCallButton?.show(false)
              //  binding?.card?.dropOffCallButton?.show(false)
                binding?.goThereButton?.show(false)
            }
        }
    }
    fun goToDestinationReached(status: OrderStatus,textStatus : String,order: Order) {
        binding?.updateStatusButton?.text = textStatus
        nextOrderState = status // not checked the payment here
        binding?.updateStatusButton?.setOnClickListener {
            navigateTo(
                OrderDetailsFragmentDirections.actionOrderDestinationReached( // to pickup reached
                    order
                ))
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