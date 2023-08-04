package com.tt.driver.ui.components.main.orders.order_details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tt.driver.databinding.ListItemWarningsBinding

class WarningsAdapter(
    private val images: List<String>
) : RecyclerView.Adapter<WarningsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ListItemWarningsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListItemWarningsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(holder.itemView).load(images[position]).into(holder.binding.icon)
    }

    override fun getItemCount() = images.size
}