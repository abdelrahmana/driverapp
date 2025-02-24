package com.tt.driver.ui.components.slots.adaptor

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tt.driver.data.models.entities.Data
import com.tt.driver.data.models.http.DataSlot
import com.tt.driver.data.models.http.Datax
import com.tt.driver.data.models.http.Slot
import com.waysgroup.speed.R
import com.waysgroup.speed.databinding.AdaptorRunSheetBinding
import com.waysgroup.speed.databinding.ExtraPriceOneItemBinding
import com.waysgroup.speed.databinding.OneItemSlotBinding


class AdaptorSlots( // one selection
    var context: Context, var arrayList: ArrayList<DataSlot>,
    val onSlotClicked: (DataSlot) -> Unit
    // var selectedArrayList: ArrayList<ModelTrip>
) :
    RecyclerView.Adapter<AdaptorSlots.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val binding = OneItemSlotBinding.inflate(LayoutInflater.from(context), parent, false)

        return ViewHolder(
            binding
        )

    }

    override fun getItemCount(): Int {

        return arrayList.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindItems(arrayList[position])

    }

    fun updateList(newArrayList: ArrayList<DataSlot>) {
        arrayList.clear()
        arrayList.addAll(newArrayList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val itemViews: OneItemSlotBinding) :
        RecyclerView.ViewHolder(itemViews.root) {
        @SuppressLint("SetTextI18n")
        fun bindItems(
            selectedItem: DataSlot?
        ) {
            itemViews.name.text = selectedItem?.customerName.toString()
            itemViews.shipmentNumber.text = selectedItem?.label.toString()

            itemViews.deliverySlot.text =
                context.getString(R.string.delivery_start,selectedItem?.deliverySlot.toString())
            itemViews.dateString.text = context.getString(R.string.delivery_date,selectedItem?.deliveryDate.toString())
            itemViews.deliveryAddress.text = context.getString(R.string.delivery_address,selectedItem?.shipmentAddress.toString())
            itemViews.mobileNumber.text = context.getString(R.string.mobile_number,selectedItem?.mobileNo.toString())
            itemViews.containerSlot.setOnClickListener {
                onSlotClicked(selectedItem!!)
            }

        }


    }

}