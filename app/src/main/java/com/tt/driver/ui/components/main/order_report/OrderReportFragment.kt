package com.tt.driver.ui.components.main.order_report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayout
import com.tt.driver.data.models.entities.OrderReport
import com.tt.driver.data.models.entities.PaymentReport
import com.tt.driver.data.models.http.OrdersReportResponse
import com.tt.driver.databinding.FragmentOrderReportBinding
import com.tt.driver.ui.base.BaseFragment
import com.tt.driver.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderReportFragment : BaseFragment<FragmentOrderReportBinding>() {

    private val viewModel by viewModels<OrderReportFragmentViewModel>()

    private var report: OrdersReportResponse.OrderReports? = null

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentOrderReportBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeResult(viewModel.report) {
            binding?.views?.show(true)
            report = it
            updateUI(getReportOfTabPosition(binding?.tabLayout?.selectedTabPosition))
        }

        binding {
            toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    updateUI(getReportOfTabPosition(tab?.position))
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}

            })

        }
    }

    private fun updateUI(data: Pair<OrderReport?, String>) {
        binding?.data = data.first
        binding?.orderSummary?.text = "You have ${data.first?.total} ${data.second}"
    }

    private fun getReportOfTabPosition(position: Int?): Pair<OrderReport?, String> {
        return when (position) {
            0 -> Pair(report?.today, "today")
            1 -> Pair(report?.week, "this week")
            else -> Pair(report?.month, "this month")
        }
    }

    override fun isLoading(status: Boolean) {
        binding?.progressBar?.show(status)
    }

}