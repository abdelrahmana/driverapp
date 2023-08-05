package com.tt.driver.ui.components.main.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tt.driver.R
import com.tt.driver.data.models.entities.Order
import com.tt.driver.databinding.ListItemOrdersBinding
import com.tt.driver.databinding.OneItemNewListOrderBinding
import com.tt.driver.ui.components.main.orders.order_utils.OrderCallActionsWrapper
import com.tt.driver.utils.show

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
            titleImageHeader.backgroundTintList = ContextCompat.getColorStateList(titleImageHeader.context!!,
                R.color.normal_order_color)
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