package com.tt.driver.ui.components.main.payments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayout
import com.tt.driver.data.models.entities.PaymentReport
import com.tt.driver.data.models.http.PaymentsReportResponse
import com.tt.driver.databinding.FragmentPaymentsBinding
import com.tt.driver.ui.base.BaseFragment
import com.tt.driver.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentsFragment : BaseFragment<FragmentPaymentsBinding>() {

    private val viewModel by viewModels<PaymentsFragmentViewModel>()

    private var report: PaymentReport? = null

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentPaymentsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.toolbar?.setNavigationOnClickListener { requireActivity().onBackPressed() }

        observeResult(viewModel.reports) {
            binding?.views?.show(true)
            report = it
            updateUI(getReportOfTabPosition(binding?.tabLayout?.selectedTabPosition))
        }

        binding?.tabLayout?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                updateUI(getReportOfTabPosition(tab?.position))
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })

    }

    fun updateUI(report: PaymentReport.Report?) {
        binding?.data = report
        binding?.availableCredit?.text = "You have credit ${report?.total?.sum} KWD"
    }

    fun getReportOfTabPosition(position: Int?): PaymentReport.Report? {
        return when (position) {
            0 -> report?.today
            1 -> report?.week
            else -> report?.month
        }
    }

    override fun isLoading(status: Boolean) {
        binding?.progressBar?.show(status)
    }

}