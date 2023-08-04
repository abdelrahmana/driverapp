package com.tt.driver.ui.components.main.orders.order_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.gcacace.signaturepad.views.SignaturePad
import com.tt.driver.R
import com.tt.driver.data.models.Failure
import com.tt.driver.data.models.Loading
import com.tt.driver.data.models.Success
import com.tt.driver.data.models.entities.OrderStatus
import com.tt.driver.databinding.FragmentDigitalSignatureBinding
import com.tt.driver.ui.base.BaseFragment
import com.tt.driver.utils.show
import dagger.hilt.android.AndroidEntryPoint
import java.util.Stack

@AndroidEntryPoint
class DigitalSignatureFragment : BaseFragment<FragmentDigitalSignatureBinding>() {

    private val viewModel by viewModels<OrderViewModel>()

    private val args by navArgs<DigitalSignatureFragmentArgs>()

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentDigitalSignatureBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.onImageUploaded.observe(viewLifecycleOwner) {
            when (it) {
                is Loading -> {
                    binding?.progressBar?.show(true)
                }
                is Success -> {
                    navigateToPaymentScreen()
                }
                is Failure -> {
                    binding?.progressBar?.show(false)
                    handleError(it.error)
                }
            }
        }

        binding {
            signaturePad.setOnSignedListener(object : SignaturePad.OnSignedListener {
                override fun onStartSigning() {
                    completeButton.isEnabled = true
                }

                override fun onSigned() {}
                override fun onClear() {
                    completeButton.isEnabled = false
                }
            })

            clear.setOnClickListener {
                signaturePad.clear()
            }

            completeButton.setOnClickListener {
                val image = signaturePad.signatureBitmap
                viewModel.uploadImage(args.orderId, image)
            }

        }

        customBackPress {  }

    }

    private fun navigateToPaymentScreen() {
        navigateBack()
    }


}