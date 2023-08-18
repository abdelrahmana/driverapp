package com.tt.driver.ui.components.main.orders.order_details

import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.tt.driver.databinding.FragmentOrderDestinationBinding
import com.tt.driver.databinding.OrderPickupNewBinding
import com.tt.driver.ui.base.MapFragment
import com.tt.driver.ui.components.main.orders.order_utils.OrderCallActionsWrapper
import com.tt.driver.utils.IntentUtils
import com.tt.driver.utils.show
import com.tt.driver.utils.showToast
import com.tt.driver.utils.toLatLng
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

@AndroidEntryPoint
class OrderPickUpFragmentReached : MapFragment<OrderPickupNewBinding>() {

    private val viewModel by viewModels<OrderViewModel>()

    private val args by navArgs<OrderDestinationFragmentArgs>()

    private var shareLink = false
    val orderCall : OrderCallActionsWrapper by lazy {
        OrderCallActionsWrapper(requireContext(),binding!!.root) }

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = OrderPickupNewBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

      /*  val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment?
        mapFragment?.getMapAsync {
            map = it
            map?.setPadding(0, 0, 0, 1200)
        }*/

        updateOrderDetailsUI(args.order)

        setActionButtonsListener()

        observePaymentURLGeneration()

    }

    private fun updateOrderStatus() {
        findNavController()
            .previousBackStackEntry
            ?.savedStateHandle
            ?.set(OrderDetailsFragment.UPDATE_ORDER_STATE, true)
    }

    private fun updateOrderDetailsUI(order: Order) {
        binding {
            userName.text = order.from_name
            orderType.text = order.delivery_category?.name
            orderPickupValueNumber.text = order.orders_count?:"0"
            dateTextPurpose.text = order.created_at + " | " + order.order_type
        /*    infoLayout.order = order
            infoLayout.callActions =
                OrderCallActionsWrapper(requireContext(), infoLayout.dropOffCallButton)

            infoLayout.date.show(false)

            warnings.adapter = WarningsAdapter(order.warnings?.map { it.image } ?: listOf())

            amount.text = order.price?.toDoubleOrNull()?.let {
                DecimalFormat("####.#").format(it)
            } ?: order.price */
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
                            updateOrderStatus()
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