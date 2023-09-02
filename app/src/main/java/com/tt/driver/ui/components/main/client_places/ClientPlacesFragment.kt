package com.tt.driver.ui.components.main.client_places

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tt.driver.ui.base.BaseFragment
import com.waysgroup.t7t_talbk_driver.databinding.FragmentClientPlacesBinding

class ClientPlacesFragment : BaseFragment<FragmentClientPlacesBinding>() {

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentClientPlacesBinding.inflate(inflater, container, false)

}