package com.tt.driver.ui.components.main.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tt.driver.ui.base.BaseFragment
import com.waysgroup.t7t_talbk_driver.databinding.FragmentNotificationsBinding

class NotificationsFragment : BaseFragment<FragmentNotificationsBinding>() {

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentNotificationsBinding.inflate(inflater, container, false)

}