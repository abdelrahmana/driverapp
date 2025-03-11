package com.tt.driver.ui.components.shipment

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tt.driver.data.models.http.Dataa
import com.tt.driver.utils.show
import com.waysgroup.speed.databinding.ReasonOneItemBinding


class AdaptorSelectedReasons( // one selection
    var context: Context, var listRejectedReasons: List<Dataa>,
    val onClicked: (Dataa) -> Unit
) :
    ListAdapter<Dataa, AdaptorSelectedReasons.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        val binding = ReasonOneItemBinding.inflate(LayoutInflater.from(context), parent, false)

        return ViewHolder(
            binding
        )

    }

    fun getListExtra(): List<Dataa> {
        return listRejectedReasons
    }

    override fun getItemCount(): Int {

        return listRejectedReasons.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bindItems(listRejectedReasons[position])

    }

    fun updateList(data: List<Dataa>) {
        listRejectedReasons = data
        Log.d("Adapter", "Received Data: ${listRejectedReasons.size}")
        submitList(listRejectedReasons)
    }

    inner class ViewHolder(val binding: ReasonOneItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bindItems(
            selectedItem: Dataa?
        ) {
            binding?.textReason?.text = selectedItem?.reasonEn
            binding?.selectedImage?.show(selectedItem?.isSelected == true)
            binding.containerReason.setOnClickListener {
                listRejectedReasons = listRejectedReasons.mapIndexed { index, item ->
                    if (adapterPosition == index)
                        item.copy(isSelected = true)
                    else
                        item.copy(isSelected = false)
                }
                notifyDataSetChanged()
            }

        }


    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Dataa>() {
            override fun areItemsTheSame(oldItem: Dataa, newItem: Dataa): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Dataa, newItem: Dataa): Boolean {
                return oldItem == newItem
            }
        }
    }
}