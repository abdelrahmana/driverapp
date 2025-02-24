package com.tt.driver.ui.components.main.contact_us

import android.view.LayoutInflater
import android.view.ViewGroup
import com.tt.driver.ui.base.BaseFragment
import com.waysgroup.speed.databinding.FragmentContactUsBinding

class ContactUsFragment : BaseFragment<FragmentContactUsBinding>() {

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentContactUsBinding.inflate(inflater, container, false)

}