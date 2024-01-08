package com.devshawon.curehealthcare.ui.fragments.filter

import android.os.Bundle
import android.view.View
import androidx.navigation.navGraphViewModels
import com.devshawon.curehealthcare.R
import com.devshawon.curehealthcare.base.ui.BaseFragment
import com.devshawon.curehealthcare.dagger.viewModel.AppViewModelFactory
import com.devshawon.curehealthcare.databinding.FilterFragmentBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject

class FilterFragment : BaseFragment<FilterFragmentBinding>(R.layout.filter_fragment) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.viewPager.adapter = ViewPagerFilterFragment(this)

        val titleTab: Array<String> = arrayOf(
            getString(R.string.company_filter),
            getString(R.string.form_filter),
        )
        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)

        TabLayoutMediator(tabLayout, mBinding.viewPager) { tab, position ->
            tab.text = titleTab[position]
        }.attach()
    }
}