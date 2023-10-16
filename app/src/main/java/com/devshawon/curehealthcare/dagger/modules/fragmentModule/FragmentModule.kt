package com.devshawon.curehealthcare.dagger.modules.fragmentModule

import androidx.lifecycle.ViewModel
import com.devshawon.curehealthcare.dagger.FragmentScoped
import com.devshawon.curehealthcare.dagger.viewModel.ViewModelKey
import com.devshawon.curehealthcare.ui.fragments.home.HomeFragment
import com.devshawon.curehealthcare.ui.fragments.home.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Suppress("unused")
@Module
internal abstract class FragmentModule {
    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindAddMoneyViewModel(viewModel: HomeViewModel): ViewModel

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeHomeFragment(): HomeFragment
}