package com.devshawon.curehealthcare.ui.fragments.trending

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.devshawon.curehealthcare.R
import com.devshawon.curehealthcare.base.ui.BaseFragment
import com.devshawon.curehealthcare.dagger.viewModel.AppViewModelFactory
import com.devshawon.curehealthcare.databinding.FragmentTrendingBinding
import com.devshawon.curehealthcare.models.ProductRequest
import com.devshawon.curehealthcare.network.Status
import com.devshawon.curehealthcare.ui.adapter.ProductAdapter
import com.devshawon.curehealthcare.ui.fragments.HomeViewModel
import com.devshawon.curehealthcare.ui.fragments.OnItemClick
import com.devshawon.curehealthcare.useCase.result.Event
import com.devshawon.curehealthcare.useCase.result.EventObserver
import kotlinx.coroutines.launch
import javax.inject.Inject

class TrendingFragment : BaseFragment<FragmentTrendingBinding>(R.layout.fragment_trending),
    OnItemClick {
    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val homeViewModel: HomeViewModel by navGraphViewModels(R.id.cure_health_care_nav_host_xml) { viewModelFactory }

    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch{
            homeViewModel.trendingRequest.postValue(Event(1))
        }
        productAdapter = ProductAdapter(onItemClick = this)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = homeViewModel
        mBinding.trending.adapter = productAdapter
        mBinding.trending.itemAnimator = DefaultItemAnimator()
        mBinding.trending.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        homeViewModel.productEvent.observe(viewLifecycleOwner, EventObserver {
            if (it == Status.SUCCESS.name) {
                productAdapter.updateProductList(homeViewModel.trendingList,1)
            }
        })
    }

    override fun onPlusIconClick(item: Int) {
    }

    override fun onMinusIconClick(item: Int) {
    }
}