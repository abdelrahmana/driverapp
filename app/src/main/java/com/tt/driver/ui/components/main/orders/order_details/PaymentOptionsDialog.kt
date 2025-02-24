package com.tt.driver.ui.components.main.orders.order_details

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.android.material.button.MaterialButton
import com.waysgroup.speed.R

class PaymentOptionsDialog(
    context: Context,
    private val onSelected: (PaymentType) -> Unit
) : AlertDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_payment_options)
        findViewById<MaterialButton>(R.id.knetButton)?.setOnClickListener {
            onSelected(PaymentType.KNET)
            dismiss()
        }
        findViewById<MaterialButton>(R.id.visButton)?.setOnClickListener {
            onSelected(PaymentType.VISA)
            dismiss()
        }
    }

}