package com.tt.driver.ui.components.main.orders.order_details

import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.SupportMapFragment
import com.tt.driver.R
import com.tt.driver.data.models.Failure
import com.tt.driver.data.models.Loading
import com.tt.driver.data.models.Success
import com.tt.driver.data.models.entities.Order
import com.tt.driver.data.models.entities.OrderStatus
import com.tt.driver.databinding.DestinationReachedBinding
import com.tt.driver.databinding.FragmentOrderDestinationBinding
import com.tt.driver.ui.base.MapFragment
import com.tt.driver.ui.components.main.orders.order_utils.OrderCallActionsWrapper
import com.tt.driver.utils.IntentUtils
import com.tt.driver.utils.Util
import com.tt.driver.utils.show
import com.tt.driver.utils.showToast
import com.tt.driver.utils.toLatLng
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

@AndroidEntryPoint
class OrderDestinationFragmentReached : MapFragment<DestinationReachedBinding>() {

    private val viewModel by viewModels<OrderViewModel>()

    private val args by navArgs<OrderDestinationFragmentArgs>()

    private var shareLink = false

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = DestinationReachedBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

      /*  val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment?
        mapFragment?.getMapAsync {
            map = it
            map?.setPadding(0, 0, 0, 1200)
        }*/
        observeResult(viewModel.order) {
            binding?.progressBar?.show(false)

                Toast.makeText(
                    requireContext(),
                    getString(R.string.order_compelete_successfully),
                    Toast.LENGTH_SHORT
                ).show()
                findNavController().popBackStack(R.id.orderDetailsFragment, false)
        }
        updateOrderDetailsUI(args.order)

        setActionButtonsListener()

        observePaymentURLGeneration()

    }

    private fun updateOrderStatus(orderStatus: OrderStatus) { // to complete order when user click pay
   /*     findNavController()
            .previousBackStackEntry
            ?.savedStateHandle
            ?.set(OrderDetailsFragment.UPDATE_ORDER_STATE, true)*/
        binding?.completeOrder?.show(true)
        binding?.completeOrder?.setOnClickListener{
            viewModel.updateOrderStatus(orderStatus)
        }
    }

    private fun updateOrderDetailsUI(order: Order) {
        binding {
            payButton.show(order.paid =="0")
            if (order.paid =="1")
            amountHeader.text = getString(R.string.amount_new)
            completeOrder.show(order.paid =="1")
            if (order.to_address.isNullOrEmpty())
                destionationLocationReached.visibility = View.GONE
            pickUpDestination.text = order.from_address?:""
            destinationLocation.text = order.to_address?:""
            dateTime.text = order.valid_date?:""
            orderPurpose.text = order.order_type?:""
            orderType.text = order.delivery_category?.name?:""
            userNameCustomerDest.text = order.to_name?:""
            userName.text = order.from_name?:""
            titleImageHeader.setCardBackgroundColor(Color.parseColor(order.color))
            callDestination.setOnClickListener{
                val url = "https://api.whatsapp.com/send?phone="+"+965"+order.from_phone?:"0"
                Util.buildImplictIntentView(requireContext(), url)
            }
            callPickUp.setOnClickListener{
                Util.startIntentAction("+965"+order?.from_phone,requireContext()!!,
                    requireActivity().getString(R.string.call), Intent.ACTION_DIAL)

            }
            /*  infoLayout.order = order
              infoLayout.callActions =
                  OrderCallActionsWrapper(requireContext(), infoLayout.dropOffCallButton)

              infoLayout.date.show(false)

              warnings.adapter = WarningsAdapter(order.warnings?.map { it.image } ?: listOf()) */

            priceValue.text = order.price?.toDoubleOrNull()?.let {
                DecimalFormat("####.#").format(it)
            } ?: order.price
        }
    }

    private fun setActionButtonsListener() {
        binding {
            payButton.setOnClickListener {
                shareLink = false
                PaymentOptionsDialog(requireContext()) {
                    when (it) {
                        PaymentType.KNET -> generatePaymentUrl(PaymentType.KNET)
                        PaymentType.VISA -> generatePaymentUrl(PaymentType.VISA)
                    }
                }.show()
            }


        }
    }

    private fun observePaymentURLGeneration() {
        viewModel.onUrlGenerated.observe(viewLifecycleOwner) {
            when (it.second) {
                is Loading -> isLoading(true)
                is Success -> {
                    isLoading(false)
                    val url = (it.second as Success).data
                    when (it.first) {
                        PaymentType.CASH -> {
                            updateOrderStatus(OrderStatus.COMPLETED)
                            navigateBack()
                        }
                        else -> {
                            if (shareLink) {
                                IntentUtils.shareUrl(requireContext(), url ?: "")
                            } else {
                                navigateTo(
                                    OrderDestinationFragmentDirections
                                        .actionOrderDestinationFragmentToOnlinePaymentFragment(url)
                                )
                            }
                        }
                    }
                }
                is Failure -> {
                    isLoading(false)
                    showToast("something went wrong")
                }
            }
        }
    }

    override fun onUserLocationFetched(location: Location) {
        pinUserLocation(location.toLatLng(), null)
        moveCameraPosition(location.toLatLng(), false)
    }

    private fun generatePaymentUrl(paymentType: PaymentType) {
        val price = args.order.price?.toDoubleOrNull() ?: 0.0
        val phone =
            if (args.order.getStatus() == OrderStatus.PENDING) args.order.from_phone else args.order.to_phone
        viewModel.generatePaymentUrl(args.order.id ?: 0, paymentType, price, phone.toString())
    }

    override fun isLoading(status: Boolean) {
        binding?.progressBar?.show(status)
    }

}