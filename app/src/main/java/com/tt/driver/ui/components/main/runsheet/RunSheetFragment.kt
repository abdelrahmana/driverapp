package com.tt.driver.ui.components.main.runsheet

import android.app.Activity
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.zxing.integration.android.IntentIntegrator
import com.tt.driver.data.models.http.RunSheetResponse
import com.tt.driver.data.models.http.Slot
import com.tt.driver.ui.base.BaseFragment
import com.tt.driver.ui.components.main.MainActivity
import com.tt.driver.ui.components.main.runsheet.adaptor.AdaptorRunSheet
import com.tt.driver.ui.components.shipment.ShipmentDetailsFragment
import com.tt.driver.ui.components.slots.SlotsFragment
import com.tt.driver.utils.Util.getCurrentDate
import com.tt.driver.utils.show
import com.waysgroup.speed.R
import com.waysgroup.speed.databinding.FragmentLoginBinding
import com.waysgroup.speed.databinding.FragmentRunSheetBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class RunSheetFragment : BaseFragment<FragmentRunSheetBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    val runSheetViewModel : RunSheetViewModel by viewModels()

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRunSheetBinding {
       return FragmentRunSheetBinding.inflate(inflater,container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        runSheetViewModel.getRunSheet(getCurrentDate())
         val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, dayOfMonth ->
                val selectedDate = "$year-${month + 1}-$dayOfMonth"
                runSheetViewModel.getRunSheet(selectedDate)

            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        binding?.calenderSelect?.setOnClickListener{
            datePicker.show()
        }
        binding?.menuIcon?.setOnClickListener {
            (requireActivity() as? MainActivity)?.openDrawer()
        }
        binding?.scannerView?.setOnClickListener{
            startBarcodeScanner()
        }
        observeResult(runSheetViewModel.runSheet) {
            binding?.progressBar?.show(false)
            updateUI(it)
        }

        binding?.scannerText?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding?.scannerText?.text.toString().trim()

                if (query.isNotEmpty()) {
                    findNavController().navigate(R.id.shipment_details, bundleOf(ShipmentDetailsFragment.SCANNERQRCODE to query))

                }

                // إخفاء الكيبورد بعد الضغط على Search
                true // إرجاع true يعني تم التعامل مع الحدث
            } else {
                false
            }
        }
    }





    private fun updateUI(it: RunSheetResponse) {
        with(binding) {
            this?.recycleViewSheets?.show(it.data.size > 0)

            this?.shipmentCard?.show(it.data.size > 0)
            this?.noResultFound?.show(it.data.size == 0)
            if (it.data.size > 0) {
                this!!.runSheetName.text = it.data?.get(0)?.name
                date.text = it.data?.get(0)?.date
                shipmentsNumber.text =
                    getString(R.string.shipment_number, it.data?.get(0)?.shipments.toString())
                val adapter = AdaptorRunSheet(
                    requireContext(),
                    (it.data.get(0).slots ?: emptyList()) as ArrayList<Slot>
                ) {
                    findNavController().navigate(
                        R.id.slotFragment,
                        bundleOf(SlotsFragment.SLOT_ID to it.id)
                    )
                }
                this.recycleViewSheets.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                this.recycleViewSheets.adapter = adapter
            } else
            // no data found
            {

            }
        }

    }
    private val barcodeLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intentResult = IntentIntegrator.parseActivityResult(result.resultCode, result.data)
            if (intentResult != null) {
                if (intentResult.contents == null) {
                    Toast.makeText(requireContext(), "Scan cancelled", Toast.LENGTH_SHORT).show()
                } else {
                    // search with qr
                    findNavController().navigate(R.id.shipment_details, bundleOf(ShipmentDetailsFragment.SCANNERQRCODE to intentResult.contents))
                }
            }
        }
    }
    private fun startBarcodeScanner() {
        val intentIntegrator = IntentIntegrator(requireActivity())
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)
        intentIntegrator.setPrompt("Scan a barcode")
        intentIntegrator.setCameraId(0) // استخدام الكاميرا الخلفية
        intentIntegrator.setBeepEnabled(true)
        intentIntegrator.setBarcodeImageEnabled(true)
        barcodeLauncher.launch(intentIntegrator.createScanIntent()) // استخدم الـ Launcher الجديد
    }


    override fun isLoading(status: Boolean) {
        binding?.progressBar?.show(status)
    }

    companion object {
    }


}