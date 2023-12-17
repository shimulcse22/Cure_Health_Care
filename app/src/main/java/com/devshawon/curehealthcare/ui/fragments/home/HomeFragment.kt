package com.devshawon.curehealthcare.ui.fragments.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import bd.com.upay.customer.network.Status
import com.devshawon.curehealthcare.R
import com.devshawon.curehealthcare.base.ui.BaseFragment
import com.devshawon.curehealthcare.dagger.viewModel.AppViewModelFactory
import com.devshawon.curehealthcare.databinding.FragmentHomeBinding
import com.devshawon.curehealthcare.ui.adapter.CommonAdapter
import com.devshawon.curehealthcare.ui.fragments.HomeViewModel
import com.devshawon.curehealthcare.useCase.result.Event
import com.devshawon.curehealthcare.useCase.result.EventObserver
import com.google.android.gms.common.internal.service.Common
import com.google.android.material.slider.Slider
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val homeViewModel: HomeViewModel by navGraphViewModels(R.id.cure_health_care_nav_host_xml) { viewModelFactory }

    private lateinit var homeBannerAdapter : CommonAdapter

    private val handler = Handler(Looper.getMainLooper())
    private var interval = 0
    private var currentPosition = 0

    private val runnable = object : Runnable {
        override fun run() {
            if (currentPosition == mBinding.bannerRecyclerView.adapter?.itemCount!! - 1) currentPosition =
                -1
            if (currentPosition < (mBinding.bannerRecyclerView.adapter?.itemCount ?: 0)) {
                mBinding.bannerRecyclerView.smoothScrollToPosition(++currentPosition)
                handler.postDelayed(this, interval.toLong())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            homeViewModel.bannerRequest.postValue(Event(Unit))
        }
        lifecycleScope.launch {
            homeBannerAdapter.updateContext(requireContext())
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = homeViewModel

        mBinding.bannerRecyclerView.adapter = homeBannerAdapter
        mBinding.bannerRecyclerView.itemAnimator = DefaultItemAnimator()
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val snapHelper: SnapHelper = PagerSnapHelper()
        mBinding.bannerRecyclerView.layoutManager = layoutManager
        mBinding.indicator.attachToRecyclerView(mBinding.bannerRecyclerView)

        if (mBinding.bannerRecyclerView.onFlingListener == null) {
            snapHelper.attachToRecyclerView(mBinding.bannerRecyclerView)
        }

        homeViewModel.event.observe(viewLifecycleOwner,EventObserver{
            if(it == Status.SUCCESS.name){
                interval = (3 * 1000)
                handler.postDelayed(runnable, interval.toLong())
                homeBannerAdapter.updateList(homeViewModel.bannerList)
            }
        })

        mBinding.bannerRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val offset: Int = mBinding.bannerRecyclerView.computeHorizontalScrollOffset()
                if (offset % mBinding.bannerRecyclerView.width == 0) {
                    currentPosition = offset / mBinding.bannerRecyclerView.width
                }
            }
        })
    }
}