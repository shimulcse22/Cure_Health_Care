package com.devshawon.curehealthcare.ui.fragments.trending

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devshawon.curehealthcare.R
import com.devshawon.curehealthcare.base.ui.BaseFragment
import com.devshawon.curehealthcare.dagger.viewModel.AppViewModelFactory
import com.devshawon.curehealthcare.databinding.FragmentTrendingBinding
import com.devshawon.curehealthcare.models.ProductData
import com.devshawon.curehealthcare.network.Status
import com.devshawon.curehealthcare.ui.CureHealthCareActivity
import com.devshawon.curehealthcare.ui.adapter.ProductAdapter
import com.devshawon.curehealthcare.ui.fragments.HomeViewModel
import com.devshawon.curehealthcare.ui.fragments.OnItemClick
import com.devshawon.curehealthcare.ui.fragments.UpdateCart
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
        lifecycleScope.launch {
            productAdapter.updateContext(requireContext())
        }

        homeViewModel.trendingPageCount.value = 0
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = homeViewModel
        mBinding.trending.adapter = productAdapter
        mBinding.trending.itemAnimator = DefaultItemAnimator()
        mBinding.trending.layoutManager = WrapContentLinearLayoutManager()

        homeViewModel.productEvent.observe(viewLifecycleOwner, EventObserver {
            (activity as CureHealthCareActivity).productListActivity.addAll(homeViewModel.trendingList)
            if (it == Status.SUCCESS.name && homeViewModel.trendingPageCount.value == 1) {
                productAdapter.updateProductList(homeViewModel.trendingList,1)
            }else{
                productAdapter.addProductList(homeViewModel.trendingList)
            }
        })

        mBinding.trending.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1) ) {
                    Log.d("THE DATA IS ^#^&^^","${homeViewModel.trendingPageCount.value}")
                    homeViewModel.trendingRequest.postValue(
                        Event(
                            homeViewModel.trendingPageCount.value!! + 1
                        )
                    )
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    override fun onPlusIconClick(item: ProductData) {
        homeViewModel.productCount.value = homeViewModel.productCount.value ?: (0 + 1)
        (activity as UpdateCart).inCreaseItem(item)
        (activity as CureHealthCareActivity).showOrHideBadge(0)
    }

    override fun onMinusIconClick(item: ProductData,position: Int) {
        homeViewModel.productCount.value = homeViewModel.productCount.value ?: (0 - 1)
        (activity as UpdateCart).decreaseItem(item,position)
        (activity as CureHealthCareActivity).showOrHideBadge(0)
    }

    inner class WrapContentLinearLayoutManager :
        LinearLayoutManager(requireContext(), VERTICAL, false) {
        override fun onLayoutChildren(
            recycler: RecyclerView.Recycler?, state: RecyclerView.State?
        ) {
            try {
                super.onLayoutChildren(recycler, state)
            } catch (_: IndexOutOfBoundsException) {

            }
        }
    }
}