package com.tt.driver.ui.components.main.orders.order_utils

import android.content.Context
import android.view.View
import com.skydoves.powermenu.PowerMenu
import com.skydoves.powermenu.PowerMenuItem
import com.tt.driver.data.models.entities.Order
import com.tt.driver.utils.IntentUtils

class OrderCallActionsWrapper(
    private val context: Context,
    private val dropDownAnchor: View
) {

    fun onSourceCall(order: Order) {
        IntentUtils.dialPhone(context, order.from_phone ?: "")
    }

    fun onDestinationCall(order: Order) {
        if (order.to_phone_extra != null) {
            PowerMenu.Builder(context).addItemList(
                listOf(PowerMenuItem(order.to_phone), PowerMenuItem(order.to_phone_extra))
            ).build().apply {
                setOnMenuItemClickListener { _, item ->
                    IntentUtils.dialPhone(context, item.title.toString())
                    dismiss()
                }
                showAsDropDown(dropDownAnchor)
            }
        } else {
            IntentUtils.dialPhone(context, order.to_phone ?: "")
        }
    }
}