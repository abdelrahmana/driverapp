package com.tt.driver.ui.components.main.orders.order_utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.tt.driver.data.models.entities.Order
import com.waysgroup.t7t_talbk_driver.R


@BindingAdapter("orderGovernorate", "isSource")
fun bindGovernorate(view: TextView, order: Order?, isSource: Boolean?) {
    view.text =
        if (isSource == true) view.context.getString(
            R.string.detailed_location,
            order?.from_region?.governorate?.name ?: "",
            order?.from_region?.name ?: ""
        )
        else view.context.getString(
            R.string.detailed_location,
            order?.to_region?.governorate?.name ?: "",
            order?.to_region?.name ?: ""
        )
}