package com.linkme.swensonhe.di

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.view.WindowManager
import com.tt.driver.utils.Constant
import com.tt.driver.utils.Util
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class,
    FragmentComponent::class,ActivityComponent::class,ServiceComponent::class)
class CommonDi {
    @Provides
    fun getUtil(@ApplicationContext context: Context?): Util {
        return Util
    }

    @Provides
    fun getSharedPrefs(@ApplicationContext context: Context?): SharedPreferences {
        return context!!.getSharedPreferences(
            Constant.SHAREDPREFS,
            Context.MODE_PRIVATE
        )
    }
}