package com.tt.driver.ui.components.registration

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.tt.driver.R
import com.tt.driver.data.datastore.AuthDataStore
import com.tt.driver.databinding.FragmentLoginBinding
import com.tt.driver.ui.base.BaseFragment
import com.tt.driver.ui.components.main.MainActivity
import com.tt.driver.utils.show
import com.tt.driver.utils.showToast
import com.tt.driver.utils.value
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val viewModel by viewModels<RegistrationViewModel>()

    @Inject
    lateinit var authDataStore: AuthDataStore

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentLoginBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding {
            Glide.with(requireContext()).load(R.drawable.appbg).into(background)

            continueButton.setOnClickListener {
                if (isValidData()) {
                    observeResult(
                        viewModel.login(phone.value(), password.value())
                    ) {
                        startActivity(Intent(requireActivity(), MainActivity::class.java))
                        authDataStore.changeAuthStatus(lifecycleScope, true)
                        requireActivity().finish()
                    }
                }
            }

        }

    }

    override fun isLoading(status: Boolean) {
        binding?.progressBar?.show(status)
    }

    private fun isValidData(): Boolean {
        if (binding?.phone?.value()?.length != 8) {
            showToast("invalid phone")
            return false
        }
        if ((binding?.password?.value()?.length?:0) < 8) {
            showToast("invalid password")
            return false
        }
        return true
    }

}