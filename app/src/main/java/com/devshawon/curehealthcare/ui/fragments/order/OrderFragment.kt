package com.devshawon.curehealthcare.ui.fragments.order

import android.os.Bundle
import android.view.View
import androidx.navigation.navGraphViewModels
import com.devshawon.curehealthcare.R
import com.devshawon.curehealthcare.base.ui.BaseFragment
import com.devshawon.curehealthcare.dagger.viewModel.AppViewModelFactory
import com.devshawon.curehealthcare.databinding.FragmentOrderBinding
import com.devshawon.curehealthcare.ui.fragments.home.HomeViewModel
import javax.inject.Inject

class OrderFragment: BaseFragment<FragmentOrderBinding>(R.layout.fragment_order) {
    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val homeViewModel: HomeViewModel by navGraphViewModels(R.id.cure_health_care_nav_host_xml) { viewModelFactory }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = homeViewModel
    }
}