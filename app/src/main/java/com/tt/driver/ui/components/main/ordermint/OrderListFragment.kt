package com.tt.driver.ui.components.main.ordermint

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tt.driver.ui.base.BaseFragment
import com.waysgroup.speed.R
import com.waysgroup.speed.databinding.FragmentOrderListBinding

class OrderListFragment : BaseFragment<FragmentOrderListBinding>() {
    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOrderListBinding {
        return  FragmentOrderListBinding.inflate(inflater,container,false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}