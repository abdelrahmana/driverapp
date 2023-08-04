package com.tt.driver.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.tt.driver.data.models.Failure
import com.tt.driver.data.models.Loading
import com.tt.driver.data.models.RemoteResult
import com.tt.driver.data.models.Success
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseFragment<T : Any> : Fragment() {

    var binding: T? = null

    fun binding(bind: T.() -> Unit) {
        bind(binding ?: throw Exception("trying to access binding outside fragment lifecycle"))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = initBinding(inflater, container)
        return (binding as? ViewBinding)?.root ?: (binding as? ViewDataBinding)?.root
    }

    abstract fun initBinding(inflater: LayoutInflater, container: ViewGroup?): T

    fun <T> observeResult(
        result: StateFlow<RemoteResult<T>>,
        customErrorHandling: ((String) -> Unit)? = null,
        successResult: (T) -> Unit
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                result.collect {
                    when (it) {
                        is Loading -> {
                            isLoading(true)
                        }
                        is Success -> {
                            isLoading(false)
                            successResult(it.data)
                        }
                        is Failure -> {
                            isLoading(false)
                            customErrorHandling?.invoke(it.error) ?: handleError(it.error)
                        }
                    }
                }
            }
        }
    }

    fun navigateTo(direction: NavDirections) {
        findNavController().navigate(direction)
    }

    fun navigateBack() {
        findNavController().navigateUp()
    }

    fun customBackPress(onBack: () -> Unit) {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    onBack()
                }
            })
    }

    protected fun handleError(error: String) {
        Snackbar.make(requireView(), error, Snackbar.LENGTH_SHORT).show()
    }

    open fun isLoading(status: Boolean) {}

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}