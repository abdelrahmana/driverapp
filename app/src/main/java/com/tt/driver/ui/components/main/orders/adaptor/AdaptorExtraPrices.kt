package com.tt.driver.ui.components.main.orders.adaptor

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tt.driver.data.models.entities.Data
import com.waysgroup.speed.R
import com.waysgroup.speed.databinding.ExtraPriceOneItemBinding


class AdaptorExtraPrices( // one selection
    var context: Context, var arrayList: ArrayList<Data>
   // var selectedArrayList: ArrayList<ModelTrip>
) :
    RecyclerView.Adapter<AdaptorExtraPrices.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val binding = ExtraPriceOneItemBinding.inflate(LayoutInflater.from(context),parent,false)

        return ViewHolder(
            binding
        )

    }
    fun getListExtra(): ArrayList<Data> {return arrayList}

    override fun getItemCount(): Int {

        return arrayList.size

    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindItems(arrayList[position])

    }

    fun setArrayList(data: List<Data>) {
        arrayList.clear()
        arrayList.addAll(data)

    }

    inner class ViewHolder(val itemViews: ExtraPriceOneItemBinding) : RecyclerView.ViewHolder(itemViews.root) {
       fun bindItems(
           selectedItem: Data?
       ) {
           itemViews.extraTitle.text =(selectedItem?.type?:"")+" "+ context?.getString(R.string.extra_price)
           itemViews.currentPrice.text =(selectedItem?.name?:"")
           itemViews.currentPrice.setOnCheckedChangeListener(object :CompoundButton.OnCheckedChangeListener{
               override fun onCheckedChanged(p0: CompoundButton?, checked: Boolean) {
                  selectedItem?.checked = checked
               }
           })

       }


   }

}