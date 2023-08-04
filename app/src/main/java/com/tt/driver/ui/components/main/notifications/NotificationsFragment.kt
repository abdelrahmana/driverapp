package com.tt.driver.ui.components.main.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tt.driver.databinding.FragmentNotificationsBinding
import com.tt.driver.ui.base.BaseFragment

class NotificationsFragment : BaseFragment<FragmentNotificationsBinding>() {

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentNotificationsBinding.inflate(inflater, container, false)

}