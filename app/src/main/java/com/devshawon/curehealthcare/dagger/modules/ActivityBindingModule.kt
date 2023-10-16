package com.devshawon.curehealthcare.dagger.modules

import com.devshawon.curehealthcare.dagger.ActivityScoped
import com.devshawon.curehealthcare.ui.CureHealthCareActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {
    @ActivityScoped
    @ContributesAndroidInjector(
        modules = [
            HealthCareModule::class
        ]
    )
    internal abstract fun mainActivity() : CureHealthCareActivity
}