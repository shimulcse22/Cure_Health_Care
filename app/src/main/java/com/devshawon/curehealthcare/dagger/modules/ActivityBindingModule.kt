package com.devshawon.curehealthcare.dagger.modules

import com.devshawon.curehealthcare.dagger.ActivityScoped
import com.devshawon.curehealthcare.dagger.modules.fragmentModule.FragmentModule
import com.devshawon.curehealthcare.ui.CureHealthCareActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            FragmentModule::class
        ]
    )
    internal abstract fun cureHealthCareActivity() : CureHealthCareActivity
}