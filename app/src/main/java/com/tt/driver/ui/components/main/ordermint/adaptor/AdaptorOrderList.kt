package com.tt.driver.ui.components.main.ordermint.adaptor

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
import com.tt.driver.utils.show
import com.waysgroup.speed.R
import com.waysgroup.speed.databinding.AdaptorRunSheetBinding
import com.waysgroup.speed.databinding.ExtraPriceOneItemBinding
import com.waysgroup.speed.databinding.OneItemOrderListBinding
import com.waysgroup.speed.databinding.OneItemSlotBinding


class AdaptorOrderList( // one selection
    var context: Context, var arrayList: ArrayList<DataSlot>,
    val onSlotClicked: (DataSlot) -> Unit
    // var selectedArrayList: ArrayList<ModelTrip>
) :
    RecyclerView.Adapter<AdaptorOrderList.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val binding = OneItemOrderListBinding.inflate(LayoutInflater.from(context), parent, false)

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

    inner class ViewHolder(val itemViews: OneItemOrderListBinding) :
        RecyclerView.ViewHolder(itemViews.root) {
        @SuppressLint("SetTextI18n")
        fun bindItems(
            selectedItem: DataSlot?
        ) {

            itemViews.root.setOnClickListener {
                onSlotClicked(selectedItem!!)
            }

        }


    }

}