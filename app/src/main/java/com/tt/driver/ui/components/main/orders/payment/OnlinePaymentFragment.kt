package com.tt.driver.ui.components.main.orders.payment

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.tt.driver.ui.base.BaseFragment
import com.tt.driver.ui.components.main.orders.order_details.OrderDetailsFragment
import com.tt.driver.utils.show
import com.waysgroup.speed.R
import com.waysgroup.speed.databinding.FragmentOnlinePaymentBinding


class OnlinePaymentFragment : BaseFragment<FragmentOnlinePaymentBinding>() {

    private val args by navArgs<OnlinePaymentFragmentArgs>()

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentOnlinePaymentBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding?.webView?.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                binding?.progressBar?.show()
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding?.progressBar?.show(false)
            }

            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                url?.let { view?.loadUrl(it) }
                return false
            }
        }
        binding?.webView?.settings?.javaScriptEnabled = true
        binding?.webView?.settings?.domStorageEnabled = true
        args.url?.let { binding?.webView?.loadUrl(it) }


        binding?.doneButton?.setOnClickListener { requireActivity().onBackPressed() }

        customBackPress {
            updateOrderStatusIfNeeded()
            findNavController().popBackStack(R.id.orderDetailsFragment, false)
        }

    }

    private fun updateOrderStatusIfNeeded() {
        findNavController()
            .getBackStackEntry(R.id.orderDetailsFragment)
            .savedStateHandle
            .set(OrderDetailsFragment.UPDATE_ORDER_STATE, false)
    }

}