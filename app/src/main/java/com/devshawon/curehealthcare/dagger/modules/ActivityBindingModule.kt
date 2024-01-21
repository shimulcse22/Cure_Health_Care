package com.devshawon.curehealthcare.dagger.modules

import com.devshawon.curehealthcare.dagger.ActivityScoped
import com.devshawon.curehealthcare.dagger.modules.fragmentModule.FragmentModule
import com.devshawon.curehealthcare.dagger.modules.loginModule.LoginModule
import com.devshawon.curehealthcare.ui.CureHealthCareActivity
import com.devshawon.curehealthcare.ui.auth.AuthActivity
import com.devshawon.curehealthcare.ui.launcher.LauncherActivity
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

    @ActivityScoped
    @ContributesAndroidInjector(modules = [LoginModule::class])
    internal abstract fun loginActivity(): AuthActivity

    @ActivityScoped
    @ContributesAndroidInjector(modules = [LoginModule::class])
    internal abstract fun launcherActivity(): LauncherActivity
}