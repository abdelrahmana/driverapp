package com.tt.driver.ui.components.main.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayout
import com.tt.driver.data.models.entities.Order
import com.tt.driver.databinding.FragmentOrdersBinding
import com.tt.driver.ui.base.BaseFragment
import com.tt.driver.utils.NestedScrollPaginationView
import com.tt.driver.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersFragment : BaseFragment<FragmentOrdersBinding>(),
    NestedScrollPaginationView.OnMyScrollChangeListener {

    private val viewModel by viewModels<OrdersViewModel>()

    private lateinit var adapter: OrdersAdapter
    var arrayListOrders = ArrayList<Order>()
    var currentPage = 1
    var hasNext = false
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
        binding?.nestedScrollPagination?.myScrollChangeListener = this
        observeResult(viewModel.order) { // when getting order
            hasNext =((currentPage == (it.data.meta?.last_page ?: 1)))
            arrayListOrders.addAll(it.data.data)
            binding?.progressBar?.show(false)
            adapter.updateList(arrayListOrders, viewModel.selectedTab == 1)
        }

        binding?.tabs?.getTabAt(viewModel.selectedTab)?.select()

        viewModel.refreshOrders(currentPage)

        binding?.tabs?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                  resetList()
                    adapter.updateList(arrayListOrders, false)
                    viewModel.selectedTab = it.position
                    binding?.progressBar?.show()
                    viewModel.refreshOrders(currentPage)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}

        })
        binding?.toolbar?.setNavigationOnClickListener { requireActivity().onBackPressed() }

    }
    fun resetList() {
        arrayListOrders.clear()
        currentPage = 1
        hasNext = true

    }
    override fun onLoadMore(currentPage: Int) {
        this.currentPage = currentPage
        binding?.progressBar?.show()
        viewModel.refreshOrders(currentPage)
    }

}