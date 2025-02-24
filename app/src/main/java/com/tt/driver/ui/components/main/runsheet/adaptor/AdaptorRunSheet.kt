package com.tt.driver.ui.components.main.runsheet.adaptor

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tt.driver.data.models.entities.Data
import com.tt.driver.data.models.http.Datax
import com.tt.driver.data.models.http.Slot
import com.waysgroup.speed.R
import com.waysgroup.speed.databinding.AdaptorRunSheetBinding
import com.waysgroup.speed.databinding.ExtraPriceOneItemBinding


class AdaptorRunSheet( // one selection
    var context: Context, var arrayList: ArrayList<Slot>,
    val onSlotClicked: (Slot) -> Unit
    // var selectedArrayList: ArrayList<ModelTrip>
) :
    RecyclerView.Adapter<AdaptorRunSheet.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val binding = AdaptorRunSheetBinding.inflate(LayoutInflater.from(context), parent, false)

        return ViewHolder(
            binding
        )

    }

    fun getListExtra(): List<Slot> {
        return arrayList
    }

    override fun getItemCount(): Int {

        return arrayList.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindItems(arrayList[position])

    }

    inner class ViewHolder(val itemViews: AdaptorRunSheetBinding) :
        RecyclerView.ViewHolder(itemViews.root) {
        @SuppressLint("SetTextI18n")
        fun bindItems(
            selectedItem: Slot?
        ) {
            itemViews.slotName.text = selectedItem?.id.toString()
            itemViews.slotTime.text =
                (selectedItem?.startTime ?: "") + " - " + (selectedItem?.endTime ?: "")
            itemViews.shipment.text = context.getString(R.string.shipment_number,selectedItem?.shipments.toString())
            itemViews.delivered.text = context.getString(R.string.delivered_number,selectedItem?.delivered.toString())
            itemViews.reject.text = context.getString(R.string.rejected_number,selectedItem?.reject.toString())
            itemViews.reschdule.text = context.getString(R.string.reschdule_number,selectedItem?.reschedule.toString())
            itemViews.containerSlots.setOnClickListener {
                onSlotClicked(selectedItem!!)
            }

        }


    }

}