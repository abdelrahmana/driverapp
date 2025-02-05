package com.tt.driver.ui.components.main.orders.order_details

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tt.driver.data.models.Failure
import com.tt.driver.data.models.Loading
import com.tt.driver.data.models.Success
import com.tt.driver.data.models.entities.Order
import com.tt.driver.data.models.entities.OrderStatus
import com.tt.driver.ui.base.MapFragment
import com.tt.driver.ui.components.main.orders.order_utils.OrderCallActionsWrapper
import com.tt.driver.utils.Constant
import com.tt.driver.utils.IntentUtils
import com.tt.driver.utils.Util
import com.tt.driver.utils.show
import com.tt.driver.utils.showToast
import com.tt.driver.utils.toLatLng
import com.waysgroup.t7t_talbk_driver.R
import com.waysgroup.t7t_talbk_driver.databinding.OrderPickupNewBinding
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException

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

    var order : Order? =null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      /*  val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment?
        mapFragment?.getMapAsync {
            map = it
            map?.setPadding(0, 0, 0, 1200)
        }*/
        order = args.order
        updateOrderDetailsUI(order!!)
        observeResult(viewModel.order) {
            binding?.progressBar?.show(false)
            order = it.data
            if (it.data?.getStatus() == OrderStatus.CANCELED_BY_CUSTOMER) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.order_canceled),
                    Toast.LENGTH_SHORT
                ).show()
               // updateOrderStatus()
                updateOrderStatusInDetails(false)
                findNavController().popBackStack(R.id.orderDetailsFragment, false)
            }
        }
        setActionButtonsListener()
        observePaymentURLGeneration(args.order)
        binding?.toolbar?.setOnClickListener {
            findNavController().popBackStack(R.id.orderDetailsFragment, false)

        }
        customBackPress {
            findNavController().popBackStack(R.id.orderDetailsFragment, false)
        }
    }
    fun updateOrderStatusInDetails(orderValue : Boolean){
        findNavController()
            .previousBackStackEntry
            ?.savedStateHandle
            ?.set(OrderDetailsFragment.UPDATE_ORDER_STATE, orderValue)
    }

    private fun updateOrderStatus() { // when payment done
     /*   findNavController()
            .previousBackStackEntry
            ?.savedStateHandle
            ?.set(OrderDetailsFragment.UPDATE_ORDER_STATE, true)*/
        viewModel.refreshOrderDetails() // to get updated order
    }
    private fun setOnClickImage() {
        if (Util.checkPermssionGrantedForImageAndFile(requireActivity(),
                registerPicResult)) // if the result ok go submit else on permssion
            Util.picPhoto(requireActivity(),registerIntentResultCamera,registerGallery)
    }
    val registerPicResult = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
    { permissions ->
        val granted = permissions.entries.all {
            it.value == true
        }
        if (granted)
            Util.picPhoto(requireActivity(), registerIntentResultCamera,registerGallery)
        else
            Util.showSnackMessages(activity, getString(R.string.cant_add_image))

    }
    var bitMap : Bitmap? =null
    private fun addImageToList(bitmapUpdatedImage: Bitmap?) {
       // val file =util.getCreatedFileFromBitmap("image",bitmapUpdatedImage!!,"jpg",requireContext())
        bitMap = bitmapUpdatedImage
        binding?.orderImage?.setImageBitmap(bitMap)

    }
    val registerIntentResultCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // There are no request codes
                val data: Intent? = result.data
                if (data?.getStringExtra(Constant.WHICHSELECTION)?: Constant.CAMERA == Constant.CAMERA)
                {
                    addImageToList( data!!.extras!!["data"] as Bitmap?)
                }
                else {
                    val contentURI = data?.data
                    try {
                        addImageToList(
                            BitmapFactory.decodeStream(requireActivity().contentResolver.openInputStream(contentURI!!)))
                        //  val file =getCreatedFileFromBitmap("image",bitmapUpdatedImage!!,"jpg",context!!)
                        //   imageModel.image?.add(file.absolutePath)
                        //.setImageBitmap(bitmapUpdatedImage)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    val registerGallery =  registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data

            val contentURI = data?.data
            try {
                addImageToList(BitmapFactory.decodeStream(requireActivity().contentResolver.openInputStream(contentURI!!))
                    )
                //  val file =getCreatedFileFromBitmap("image",bitmapUpdatedImage!!,"jpg",context!!)
                //   imageModel.image?.add(file.absolutePath)
                //.setImageBitmap(bitmapUpdatedImage)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }


    private fun updateOrderDetailsUI(order: Order) {
        binding {
            userName.text = order.from_name
            orderType.text = order.delivery_category?.name
            orderPickupValueNumber.text = order.orders_count?:"0"
            dateTextPurpose.text = order.valid_date + " | " + order.order_type
            orderPickUpNext.setOnClickListener{
                bitMap?.let { it-> viewModel.uploadImage(order.id?:0,it)}?: kotlin.run{
                    navigateTo(
                        OrderPickUpFragmentReachedDirections.actionDestinationPreviewFromOrdeTwo( // to pickup reached
                            this@OrderPickUpFragmentReached.order!!
                        ))
                }
            }
            callDestination.setOnClickListener{
                val url = "https://api.whatsapp.com/send?phone="+"+965"+order.from_phone?:"0"
                Util.buildImplictIntentView(requireContext(), url)
            }
            callPickUp.setOnClickListener{
                Util.startIntentAction("+965"+order?.from_phone,requireContext()!!,
                    requireActivity().getString(R.string.call), Intent.ACTION_DIAL)

            }

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
            payButton.show(order?.paid =="0")
            payButton.setOnClickListener {
                shareLink = false
                PaymentOptionsDialog(requireContext()) {
                    when (it) {
                        PaymentType.KNET -> generatePaymentUrl(PaymentType.KNET)
                        PaymentType.VISA -> generatePaymentUrl(PaymentType.VISA)
                        else -> {}
                    }
                }.show()
            }
            cancelButton.setOnClickListener{
                viewModel.updateOrderStatus(OrderStatus.CANCELED_BY_CUSTOMER,null,null,null,order?.id)
             //   findNavController().popBackStack(R.id.orderDetailsFragment, false)
            }
            orderImage.setOnClickListener{
                setOnClickImage()

            }

        }
    }

    private fun observePaymentURLGeneration(order: Order) {
        viewModel.onUrlGenerated.observe(viewLifecycleOwner) {
            when (it.second) {
                is Loading ->binding?.progressBar?.show(true)
// isLoading(true)
                is Success -> {
                    //isLoading(false)
                    binding?.progressBar?.show(false)
                    val url = (it.second as Success).data
                    when (it.first) {
                        PaymentType.CASH -> {
                            updateOrderStatus()
                           // navigateBack()
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
                    binding?.progressBar?.show(false)
                   // isLoading(false)
                    showToast("something went wrong")
                }
            }
        }
        viewModel.onImageUploaded.observe(viewLifecycleOwner) {
            when (it) {
                is Loading -> {
                    binding?.progressBar?.show(true)
                }
                is Success -> {
                    binding?.progressBar?.show(false)
                    //  findNavController().navigate()
                    order.imagePath = Util.getCreatedFileFromBitmap("image",bitMap!!,"jpg",requireContext()).absolutePath
                    navigateTo(
                        OrderPickUpFragmentReachedDirections.actionDestinationPreviewFromOrdeTwo( // to pickup reached
                            order
                        ))
                }
                is Failure -> {
                    binding?.progressBar?.show(false)
                    handleError(it.error)
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
      //  binding?.progressBar?.show(status)
    }

}