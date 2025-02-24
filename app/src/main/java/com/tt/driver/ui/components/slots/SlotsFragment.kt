package com.tt.driver.ui.components.slots

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.tt.driver.data.models.entities.Data
import com.tt.driver.data.models.http.DataSlot
import com.tt.driver.ui.base.BaseFragment
import com.tt.driver.ui.components.slots.adaptor.AdaptorSlots
import com.tt.driver.utils.Util
import com.tt.driver.utils.show
import com.waysgroup.speed.databinding.FragmentSlotsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SlotsFragment : BaseFragment<FragmentSlotsBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    val viewModel : SlotsViewModel by viewModels()
    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSlotsBinding {
        return FragmentSlotsBinding.inflate(inflater,container, false)

    }
    var adaptor : AdaptorSlots? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adaptor = AdaptorSlots(requireContext(), arrayListOf()){
            // redirect to start shipment page
        }

        observeResult(viewModel.slot) {
            // in success
            adaptor?.updateList(it.data as ArrayList<DataSlot>)
            binding?.progressBar?.show(false)
            binding?.noResultFound?.show(it.data.isEmpty())

        }
        binding?.tabs?.getTabAt(viewModel.selectedTab)?.select()
        viewModel.getSlotDependOnType(
            arguments?.getInt(SLOT_ID)?:0,
            SlotType.entries.find { it.ordinal == binding?.tabs?.selectedTabPosition }?.type ?: ""
        )
        with(binding!!) {
            // set recycleview items
            Util.setRecycleView(slotsRecycleView,adaptor!!,LinearLayoutManager.VERTICAL,requireContext(),false)
            binding?.tabs?.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.let {
                        adaptor?.updateList(arrayListOf())
                        viewModel.selectedTab = it.position
                        binding?.progressBar?.show()
                        viewModel.getSlotDependOnType(
                            arguments?.getInt(SLOT_ID)?:0,
                            SlotType.entries.find { it.ordinal == tab.position }?.type ?: "",
                        )
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}

            })
            binding?.toolbar?.setNavigationOnClickListener { requireActivity().onBackPressed() }
            removeCapsFromAllTabs(tabs)
        }
    }
    fun removeCapsFromAllTabs(tabLayout: TabLayout){
        for (i in 0 until tabLayout.tabCount) {
            val tab = tabLayout.getTabAt(i)
            val textView = TextView(requireContext())
            textView.text = tab?.text
            textView.isAllCaps = false  // هنا التعطيل النهائي للحروف الكبيرة
            textView.gravity = Gravity.CENTER  // ضبط النص في المنتصف
            textView.textAlignment = View.TEXT_ALIGNMENT_CENTER

            // ضبط الـ LayoutParams علشان النص يكون في المنتصف تمامًا
            textView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER
            }
            tab?.customView = textView
        }
    }
        override fun isLoading(status: Boolean) {
            binding?.progressBar?.show(status)
        }
    companion object {
        val SLOT_ID= "slot_id"
    }
    }

