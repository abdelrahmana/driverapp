package com.tt.driver.ui.components.shipment

import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.tt.driver.data.models.entities.OrderStatus
import com.tt.driver.data.models.http.ShipmentDetailsResponse
import com.tt.driver.ui.base.LocationAwareFragment
import com.tt.driver.ui.components.main.MainActivity
import com.tt.driver.utils.Util
import com.tt.driver.utils.show
import com.waysgroup.speed.R
import com.waysgroup.speed.databinding.ShipmentDetailsFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShipmentDetailsFragment : LocationAwareFragment<ShipmentDetailsFragmentBinding>() {

    private val viewModel by viewModels<ShipmentViewModel>()

    private var currentLocation: Location? = null

    private var nextOrderState: OrderStatus? = null;
    var whichSelection =""
    companion object {
        const val UPDATE_ORDER_STATE = "UPDATE_ORDER_STATE"
        const val SHIPMENT_ID = "Shipment_ID"
        const val SCANNERQRCODE: String = "scanner_qr"
        const val PENDING = "out_of_delivery";
        const val DELIVERED = "delivered";
        const val REJECTED = "rejected";
        const val RESCHEDULED = "rescheduled";
    }

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = ShipmentDetailsFragmentBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.toolbar?.setOnClickListener { requireActivity().onBackPressed() }
        arguments?.getString(SCANNERQRCODE)?.let { labelShipment ->
            viewModel.getShipmentByQrCode(HashMap<String, Any>().also {
                it.put("barcode", arguments?.getString(SCANNERQRCODE) ?: "")
            })
        } ?: viewModel.getShipment(arguments?.getInt(SHIPMENT_ID) ?: 0, HashMap())
        observeResult(viewModel.shipmentStateFlow, customErrorHandling = {
            binding?.container?.show(false)
            binding?.imageMap?.show(false)
            binding?.goThereButton?.show(false)
            binding?.noResultFound?.show(true)
        }, {
            binding?.progressBar?.show(false)
            updateUI(it)
        })
        observeResult(viewModel.shipmentUpdateStatus) {
            binding?.progressBar?.show(false)
            Toast.makeText(
                requireContext(),
                getString(R.string.order_compelete_successfully),
                Toast.LENGTH_SHORT
            ).show()
            /* */
            if (whichSelection == DELIVERED)
                navigateTo(
                    ShipmentDetailsFragmentDirections.actionShipmentDetailsFragmentToDigitalSignatureFragment(
                        arguments?.getInt(SHIPMENT_ID) ?: 0
                    )
                )
            else {
                requireActivity().startActivity(Intent(requireActivity(), MainActivity::class.java))
                requireActivity().finish()
            }


        }
    }

    private fun updateUI(shipmentResponse: ShipmentDetailsResponse) {
        val shipment = shipmentResponse.data ?: return

        binding?.run {

            shipmentNameValue.text = shipment.shipmentFrom ?: ""
            shipmentDetailsValue.text = shipment.shipmentDetails ?: ""
            shipmentText.text = shipment.label ?: ""

            container.show()
            nameCustomer.text = shipment.customer ?: ""
            goveranteValuePickUp.text = shipment.to_governorate?.name ?: ""
            regionValuePickup.text = shipment.to_region?.name ?: ""
            blockValuePickUp.text = shipment.to_block?.name ?: ""
            homePickUpValue.text = shipment.to_home_number ?: ""
            apartmentPickUp.text = shipment.to_apartment_number ?: ""
            floorNumberValue.text = shipment.to_floor_number ?: ""
            streetPickupValue.text = shipment.to_address ?: ""

            streetPickupValue.show(shipment.to_address != null)
            streetPickup.show(shipment.to_address != null)
            callDestination.show(shipment.to_phone != null)
            callPickUp.show(shipment.to_phone != null)
            floorNumberValue.show(shipment.to_floor_number != null)
            floorNo.show(shipment.to_floor_number != null)
            apartmentPickUp.show(shipment.to_apartment_number != null)
            homePickUpValue.show(shipment.to_home_number != null)
            regionValuePickup.show(shipment.to_region != null)
            goveranteValuePickUp.show(shipment.to_governorate != null)
            noteHeader.show(shipment.notes !=null || shipment.callCenter!=null)
            noteOne.text = shipment.notes ?: ""
            noteII.text = shipment.callCenter ?: ""
            checkDeliveryStatus(shipment.deliveryStatus,shipment.id,shipment.to_lat,shipment.to_long)

            callDestination.setOnClickListener {
                val url = "https://api.whatsapp.com/send?phone=" + "+965" + shipment.to_phone ?: "0"
                Util.buildImplictIntentView(requireContext(), url)
            }
            callPickUp.setOnClickListener {
                Util.startIntentAction(
                    "+965" + shipment?.to_phone, requireContext()!!,
                    requireActivity()!!.getString(R.string.call), Intent.ACTION_DIAL
                )
            }
            /*    order.notes?.let { notes ->
                    card.alert.show()
                    card.alertMessage.text = notes
                }*/


        }

    }

    private fun checkDeliveryStatus(
        deliveryStatus: String?,
        id: Int?,
        toLat: String?,
        toLong: String?
    ) {
        binding?.run {
            goThereButton.show(deliveryStatus == PENDING)
            imageMap.show(deliveryStatus == PENDING)
            goThereButton.setOnClickListener{
                imageMap.show(false)
                goThereButton.show(false)
                deliveredContainer.show(goThereButton.visibility == View.GONE)
                reschduleContainer.show(goThereButton.visibility == View.GONE)
                rejectedContainer.show(goThereButton.visibility == View.GONE)

            }
            imageMap.setOnClickListener{
                val gmmIntentUri = Uri.parse("google.navigation:q=$toLat,$toLong")
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            }
            deliveredContainer.setOnClickListener{
                whichSelection = DELIVERED
                viewModel.updateShipmentStatus(HashMap<String, Any>().also {
                    it.put("order_id",id?:0)
                    it.put("status", DELIVERED)
                    it.put("delivery_status_lat",currentLocation?.latitude?:0.0)
                    it.put("delivery_status_lng",currentLocation?.longitude?:0.0)

                })
            }
            reschduleContainer.setOnClickListener{
                whichSelection = RESCHEDULED
                viewModel.updateShipmentStatus(HashMap<String, Any>().also {
                    it.put("order_id",id?:0)
                    it.put("status", RESCHEDULED)
                    it.put("delivery_status_lat",currentLocation?.latitude?:0.0)
                    it.put("delivery_status_lng",currentLocation?.longitude?:0.0)
                })
            }
            rejectedContainer.setOnClickListener{
                 DialogConfirmActions().also {
                    it.setCallBack {selectedId->
                        whichSelection = REJECTED
                        viewModel.updateShipmentStatus(HashMap<String, Any>().also {
                            it.put("order_id",id?:0)
                            it.put("status", REJECTED)
                            it.put("reject_reason_id",selectedId)
                            it.put("delivery_status_lat",currentLocation?.latitude?:0.0)
                            it.put("delivery_status_lng",currentLocation?.longitude?:0.0)
                        })

                    }
                     it.show(requireActivity().supportFragmentManager, "DialogConfirmActions")
                }

            }
        }

    }
    override fun onLocationPermissionsSatisfied() {
        super.onLocationPermissionsSatisfied()
        (requireActivity() as? MainActivity)?.startLocationTrackingService()
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

}