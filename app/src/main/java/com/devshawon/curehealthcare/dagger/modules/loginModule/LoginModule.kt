package com.devshawon.curehealthcare.dagger.modules.loginModule

import androidx.lifecycle.ViewModel
import com.devshawon.curehealthcare.dagger.FragmentScoped
import com.devshawon.curehealthcare.dagger.viewModel.ViewModelKey
import com.devshawon.curehealthcare.ui.auth.AuthViewModel
import com.devshawon.curehealthcare.ui.auth.fragments.LoginFragment

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
internal abstract class LoginModule {

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun bindLoginViewModel(viewModel: AuthViewModel): ViewModel

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeLoginFragment(): LoginFragment
}

