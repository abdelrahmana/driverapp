package com.tt.driver.ui.components.main.orders

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tt.driver.data.models.entities.Order
import com.tt.driver.ui.components.main.orders.order_utils.OrderCallActionsWrapper
import com.tt.driver.utils.show
import com.waysgroup.speed.databinding.OneItemNewListOrderBinding

class OrdersAdapter(
    private val onOrderClicked: (Order) -> Unit
) : RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {

    private var orders: List<Order> = listOf()

    private var isHistory = false

    fun updateList(list: List<Order>, isHistoryOrders: Boolean) {
        orders = list
        isHistory = isHistoryOrders
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            OneItemNewListOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            val order = orders[position]
            // this should be changed depend on the order status
            titleImageHeader.setCardBackgroundColor(Color.parseColor(order.color))//ContextCompat.getColorStateList(titleImageHeader.context!!,
            dateTime.text = order.time?:""
            purpose.text = order.order_type?:""
            orderName.text = order.delivery_category?.name?:""
            pickUpDestination.text = order.pick_up_address?:""
            destinationLocation.text = order.destination_address?:""
            if (order.destination_address==null)
                containerDestinations.visibility = View.GONE
            /*   this.order = order
               this.callActions = OrderCallActionsWrapper(
                   holder.binding.root.context,
                   holder.binding.dropOffCallButton
               )
               pickUpCallButton.show(!isHistory)
               dropOffCallButton.show(!isHistory)*/
            root.setOnClickListener { onOrderClicked(order) }
        }
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    class ViewHolder(val binding: OneItemNewListOrderBinding) :
        RecyclerView.ViewHolder(binding.root)
}