package com.tt.driver.ui.components.shipment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tt.driver.data.models.Failure
import com.tt.driver.data.models.Loading
import com.tt.driver.data.models.Success
import com.tt.driver.ui.components.main.MainActivity
import com.tt.driver.ui.components.shipment.ShipmentDetailsFragment.Companion.DELIVERED
import com.tt.driver.ui.components.shipment.ShipmentDetailsFragment.Companion.REJECTED
import com.tt.driver.ui.components.shipment.ShipmentDetailsFragment.Companion.SHIPMENT_ID
import com.tt.driver.utils.Util
import com.tt.driver.utils.show
import com.waysgroup.speed.R
import com.waysgroup.speed.databinding.DialogReasonFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DialogConfirmActions : BottomSheetDialogFragment() {

    private val shipmentViewModel  : ShipmentViewModel by viewModels()
    private var callBack : ((Int) -> Unit)? =null
    fun setCallBack(callBack : ((Int) -> Unit)){
        this.callBack = callBack
    }
    var binding : DialogReasonFragmentBinding? =null
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // for making the bottom sheet background transparent
        //  webService = ApiManagerDefault(activity!!).apiService
        setStyle(STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogReasonFragmentBinding.inflate(layoutInflater,container,false)
        return binding?.root
    }

    var adaptor : AdaptorSelectedReasons? =null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shipmentViewModel.getReasonsRejection()
        adaptor = AdaptorSelectedReasons(requireContext(), emptyList()){

        }
        observeRejection()
        binding?.confirmButton?.setOnClickListener{
            val selected = adaptor?.listRejectedReasons?.find { it.isSelected }
            selected?.let {
                // in case there is a selection
                callBack?.invoke(selected.id)
                dismiss()
            }
        }


    }

    private fun observeRejection() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                shipmentViewModel.getReasons.collect {
                    when (it) {
                        is Loading -> {
                        }
                        is Success -> {
                            // load adaptor
                            Util.setRecycleView(binding?.rejectedRecycle,adaptor!!,LinearLayoutManager.VERTICAL,requireContext(),false)
                            adaptor?.updateList(it.data.data)

                        }
                        is Failure -> {
                          Toast.makeText(requireContext(),it.error,Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
       // dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

}

