package com.tt.driver.ui.components.shipment

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.tt.driver.data.models.entities.OrderStatus
import com.tt.driver.data.models.http.ShipmentDetailsResponse
import com.tt.driver.ui.base.LocationAwareFragment
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

    companion object {
        const val UPDATE_ORDER_STATE = "UPDATE_ORDER_STATE"
        const val SHIPMENT_ID = "Shipment_ID"
        const val SCANNERQRCODE : String = "scanner_qr"
    }
    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = ShipmentDetailsFragmentBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.getString(SCANNERQRCODE)?.let { labelShipment->
            viewModel.getShipmentByQrCode(HashMap<String, Any>().also {
                it.put("barcode",arguments?.getString(SCANNERQRCODE)?:"")
            })
        }?:viewModel.getShipment(arguments?.getInt(SHIPMENT_ID)?:0,HashMap())
        observeResult(viewModel.shipmentStateFlow, customErrorHandling = {
            binding?.container?.show(false)
            binding?.imageMap?.show(false)
            binding?.goThereButton?.show(false)
            binding?.noResultFound?.show(true)
        },{
            binding?.progressBar?.show(false)
            updateUI(it)
        })
        binding?.toolbar?.setOnClickListener { requireActivity().onBackPressed() }

    }

    private fun updateUI(shipmentResponse: ShipmentDetailsResponse) {
        val shipment = shipmentResponse.data ?: return

        binding?.run {

            shipmentNameValue.text = shipment.shipmentFrom?:""
            shipmentDetailsValue.text = shipment.shipmentDetails?:""
            shipmentText.text = shipment.label?:""

            container.show()
            nameCustomer.text = shipment.customer?:""
            goveranteValuePickUp.text = shipment.to_governorate?.name?:""
            regionValuePickup.text = shipment.to_region?.name?:""
            blockValuePickUp.text = shipment.to_block?.name?:""
            homePickUpValue.text = shipment.to_home_number?:""
            apartmentPickUp.text = shipment.to_apartment_number?:""
            floorNumberValue.text = shipment.to_floor_number?:""
            streetPickupValue.text = shipment.to_address?:""

            streetPickupValue.show(shipment.to_address!=null)
            streetPickup.show(shipment.to_address!=null)
            callDestination.show(shipment.to_phone!=null)
            callPickUp.show(shipment.to_phone!=null)
            floorNumberValue.show(shipment.to_floor_number!=null)
            floorNo.show(shipment.to_floor_number!=null)
            apartmentPickUp.show(shipment.to_apartment_number!=null)
            homePickUpValue.show(shipment.to_home_number!=null)
            regionValuePickup.show(shipment.to_region!=null)
            goveranteValuePickUp.show(shipment.to_governorate!=null)

            callDestination.setOnClickListener{
                val url = "https://api.whatsapp.com/send?phone="+"+965"+shipment.to_phone?:"0"
                Util.buildImplictIntentView(requireContext(), url)
            }
            callPickUp.setOnClickListener{
                Util.startIntentAction("+965"+shipment?.to_phone,requireContext()!!,
                    requireActivity()!!.getString(R.string.call), Intent.ACTION_DIAL)
            }
        /*    order.notes?.let { notes ->
                card.alert.show()
                card.alertMessage.text = notes
            }*/

            binding?.goThereButton?.setOnClickListener {
               // show option select delivered or reject
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

}