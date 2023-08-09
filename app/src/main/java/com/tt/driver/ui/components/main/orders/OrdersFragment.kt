package com.tt.driver.ui.components.main.orders

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayout
import com.tt.driver.data.models.entities.OrderStatus
import com.tt.driver.databinding.FragmentOrdersBinding
import com.tt.driver.ui.base.BaseFragment
import com.tt.driver.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersFragment : BaseFragment<FragmentOrdersBinding>() {

    private val viewModel by viewModels<OrdersViewModel>()

    private lateinit var adapter: OrdersAdapter

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentOrdersBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = OrdersAdapter {
            navigateTo(OrdersFragmentDirections.actionOrdersFragmentToOrderDetailsFragment(it.id ?: 0))
        }
        binding?.ordersList?.adapter = adapter

        observeResult(viewModel.order) { // when getting order
            binding?.progressBar?.show(false)
            adapter.updateList(it, viewModel.selectedTab == 1)
        }

        binding?.tabs?.getTabAt(viewModel.selectedTab)?.select()

        viewModel.refreshOrders()

        binding?.tabs?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    adapter.updateList(listOf(), false)
                    viewModel.selectedTab = it.position
                    binding?.progressBar?.show()
                    viewModel.refreshOrders()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })

        binding?.toolbar?.setNavigationOnClickListener { requireActivity().onBackPressed() }

    }

}