package com.tt.driver.ui.components.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.tt.driver.ui.base.BaseFragment
import com.waysgroup.t7t_talbk_driver.R
import com.waysgroup.t7t_talbk_driver.databinding.FragmentOnboardingBinding

class OnBoardingFragment : BaseFragment<FragmentOnboardingBinding>() {

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentOnboardingBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding {
            Glide.with(requireContext()).load(R.drawable.appbg).into(background)
            loginButton.setOnClickListener {
                navigateTo(OnBoardingFragmentDirections.actionOnBoardingFragmentToLoginFragment())
            }
        }
    }

}