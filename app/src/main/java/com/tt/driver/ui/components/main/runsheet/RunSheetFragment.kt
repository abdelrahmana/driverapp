package com.tt.driver.ui.components.main.runsheet

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.tt.driver.data.models.http.RunSheetResponse
import com.tt.driver.data.models.http.Slot
import com.tt.driver.ui.base.BaseFragment
import com.tt.driver.ui.components.main.MainActivity
import com.tt.driver.ui.components.main.runsheet.adaptor.AdaptorRunSheet
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
        observeResult(runSheetViewModel.runSheet) {
            binding?.progressBar?.show(false)
            updateUI(it)
        }

    }

    private fun updateUI(it: RunSheetResponse) {
        with(binding){
            this?.recycleViewSheets?.show(it.data.size>0)

            this?.shipmentCard?.show(it.data.size>0)
            this?.noResultFound?.show(it.data.size == 0)
            if (it.data.size>0) {
                this!!.runSheetName.text = it.data?.get(0)?.name
                date.text = it.data?.get(0)?.date
                shipmentsNumber.text = getString(R.string.shipment_number,it.data?.get(0)?.shipments.toString())
                val adapter = AdaptorRunSheet(requireContext(),
                    (it.data.get(0).slots?: emptyList()) as ArrayList<Slot>
                ){
                    findNavController().navigate(R.id.slotFragment, bundleOf(SlotsFragment.SLOT_ID to it.id))
                }
                this.recycleViewSheets.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                this.recycleViewSheets.adapter = adapter
            } else
                // no data found

            {

            }
        }
    }

    override fun isLoading(status: Boolean) {
        binding?.progressBar?.show(status)
    }

    companion object {
    }


}