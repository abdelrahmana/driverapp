package com.tt.driver.ui.components.main.setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.internal.RegisterListenerMethod
import com.tt.driver.data.datastore.AuthDataStore
import com.tt.driver.ui.base.BaseFragment
import com.tt.driver.ui.components.main.MainActivity
import com.tt.driver.ui.components.registration.RegistrationActivity
import com.waysgroup.speed.R
import com.waysgroup.speed.databinding.FragmentNotificationsBinding
import com.waysgroup.speed.databinding.SettingLayoutFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingFragment : BaseFragment<SettingLayoutFragmentBinding>() {
    @Inject
    lateinit var authDataStore: AuthDataStore
    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = SettingLayoutFragmentBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.logOutContainer?.setOnClickListener{
            authDataStore.changeAuthStatus(lifecycleScope, false)
            startActivity(Intent(requireActivity(), RegistrationActivity::class.java))
            requireActivity().finish()
        }
        binding!!.changeLangContainer.setOnClickListener{
            findNavController().navigate(R.id.languageFragment)
        }
    }
}
