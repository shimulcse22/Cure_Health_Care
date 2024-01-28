package com.devshawon.curehealthcare.dagger.modules.fragmentModule

import androidx.lifecycle.ViewModel
import com.devshawon.curehealthcare.dagger.FragmentScoped
import com.devshawon.curehealthcare.dagger.viewModel.ViewModelKey
import com.devshawon.curehealthcare.ui.auth.fragments.LoginFragment
import com.devshawon.curehealthcare.ui.auth.fragments.RegistrationFragment
import com.devshawon.curehealthcare.ui.fragments.cart.CartFragment
import com.devshawon.curehealthcare.ui.fragments.order.OrderFragment
import com.devshawon.curehealthcare.ui.fragments.home.HomeFragment
import com.devshawon.curehealthcare.ui.fragments.HomeViewModel
import com.devshawon.curehealthcare.ui.fragments.filter.CompanyFilterFragment
import com.devshawon.curehealthcare.ui.fragments.filter.FilterFragment
import com.devshawon.curehealthcare.ui.fragments.filter.FilterViewModel
import com.devshawon.curehealthcare.ui.fragments.filter.FormFilterFragment
import com.devshawon.curehealthcare.ui.fragments.filter.NotificationFragment
import com.devshawon.curehealthcare.ui.fragments.filter.SearchFragment
import com.devshawon.curehealthcare.ui.fragments.order.SingleOrderFragment
import com.devshawon.curehealthcare.ui.fragments.profile.ChangePassword
import com.devshawon.curehealthcare.ui.fragments.profile.EditProfileFragment
import com.devshawon.curehealthcare.ui.fragments.profile.ProfileFragment
import com.devshawon.curehealthcare.ui.fragments.trending.TrendingFragment
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

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeTrendingFragment(): TrendingFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeOrderFragment(): OrderFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeCartFragment(): CartFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeProfileFragment(): ProfileFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeEditProfileFragment(): EditProfileFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeChangePassword(): ChangePassword

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeFilterFragment(): FilterFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeCompanyFilterFragment(): CompanyFilterFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeFormFilterFragment(): FormFilterFragment

    @Binds
    @IntoMap
    @ViewModelKey(FilterViewModel::class)
    abstract fun bindFilterViewModel(viewModel: FilterViewModel): ViewModel

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeSearchFragment(): SearchFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeNotificationFragment(): NotificationFragment

    @FragmentScoped
    @ContributesAndroidInjector
    internal abstract fun contributeSingleOrderFragment(): SingleOrderFragment

}