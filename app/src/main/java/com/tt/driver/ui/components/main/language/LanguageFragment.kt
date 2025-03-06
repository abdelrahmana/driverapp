package com.tt.driver.ui.components.main.language

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tt.driver.ui.SplashActivity
import com.tt.driver.utils.Constant
import com.tt.driver.utils.Util
import com.waysgroup.speed.R
import com.waysgroup.speed.databinding.FragmentLanguageBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LanguageFragment : Fragment() {
    lateinit var binding : FragmentLanguageBinding
    @Inject lateinit var sharedPrefs : SharedPreferences
    @Inject lateinit var util : Util
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLanguageBinding.inflate(layoutInflater,container,false)

        binding.back.text = getString(R.string.language)
        binding.back.setOnClickListener{
            requireActivity().onBackPressed()
        }
        if ((sharedPrefs.getString(Constant.LOCALE_LANGUAGE, "en") ?: "en") == "en")
            binding.englishCheck.setImageResource(R.drawable.ic_checked)
        else
            binding.arabicCheck.setImageResource(R.drawable.ic_checked)
        setonClickListener(binding.containerEnglish,"en")
        setonClickListener(binding.containerArabic,"ar")


        // Inflate the layout for this fragment
        return binding.root
    }
    private fun setonClickListener(containerView: View, newLang: String) {
        containerView.setOnClickListener{
            sharedPrefs.edit().putString(Constant.LOCALE_LANGUAGE,newLang).apply()
            util.setLanguagePerActivity(requireActivity(),
                Intent(activity, SplashActivity::class.java),sharedPrefs
            )
        }
    }
}