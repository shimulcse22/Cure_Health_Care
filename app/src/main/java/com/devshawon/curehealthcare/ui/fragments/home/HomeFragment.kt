package com.devshawon.curehealthcare.ui.fragments.home

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.devshawon.curehealthcare.network.Status
import com.devshawon.curehealthcare.R
import com.devshawon.curehealthcare.base.ui.BaseFragment
import com.devshawon.curehealthcare.dagger.viewModel.AppViewModelFactory
import com.devshawon.curehealthcare.databinding.FragmentHomeBinding
import com.devshawon.curehealthcare.ui.adapter.CommonAdapter
import com.devshawon.curehealthcare.ui.fragments.HomeViewModel
import com.devshawon.curehealthcare.useCase.result.Event
import com.devshawon.curehealthcare.useCase.result.EventObserver
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val homeViewModel: HomeViewModel by navGraphViewModels(R.id.cure_health_care_nav_host_xml) { viewModelFactory }

    private lateinit var homeBannerAdapter: CommonAdapter

    private val handler = Handler(Looper.getMainLooper())
    private var interval = 0
    private var currentPosition = 0

    private val runnable = object : Runnable {
        override fun run() {

            if (currentPosition == (mBinding.bannerRecyclerView.adapter?.itemCount
                    ?: (1 - 1))
            ) currentPosition = -1
            if (currentPosition < (mBinding.bannerRecyclerView.adapter?.itemCount ?: 0)) {
                mBinding.bannerRecyclerView.smoothScrollToPosition(++currentPosition)
                handler.postDelayed(this, interval.toLong())
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBannerAdapter = CommonAdapter()
        lifecycleScope.launch {
            homeViewModel.bannerRequest.postValue(Event(Unit))
        }
        lifecycleScope.launch {
            homeBannerAdapter.updateContext(requireContext())
        }
        lifecycleScope.launch{
            homeViewModel.productRequest.postValue(Event("tab"))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = homeViewModel
        handler.removeCallbacks(runnable)
        runnable.run()

        addRadioButtons()

        mBinding.bannerRecyclerView.adapter = homeBannerAdapter
        mBinding.bannerRecyclerView.itemAnimator = DefaultItemAnimator()
        var layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val snapHelper: SnapHelper = PagerSnapHelper()
        mBinding.bannerRecyclerView.layoutManager = layoutManager
        mBinding.indicator.attachToRecyclerView(mBinding.bannerRecyclerView)

        if (mBinding.bannerRecyclerView.onFlingListener == null) {
            snapHelper.attachToRecyclerView(mBinding.bannerRecyclerView)
        }

        homeViewModel.event.observe(viewLifecycleOwner, EventObserver {
            if (it == Status.SUCCESS.name) {
                interval = (3 * 1000)
                handler.postDelayed(runnable, interval.toLong())
                homeBannerAdapter.updateList(homeViewModel.bannerList,0)
            }
        })

        homeViewModel.productEvent.observe(viewLifecycleOwner, EventObserver {
            if (it == Status.SUCCESS.name) {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                homeBannerAdapter.updateProductList(homeViewModel.productList,1)
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

    private fun addRadioButtons() {
        mBinding.radioButton.orientation = LinearLayout.HORIZONTAL
        val params = RadioGroup.LayoutParams(
            RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT
        )
        for (i in 0 until 27) {
            val radioButton = RadioButton(requireContext())
            radioButton.apply {
                id = i
                params.setMargins(8, 5, 8, 5)
                //setBackgroundResource(R.drawable.radio_button_background)
                setPadding(2, 1, 2, 1)
                text = if (i == 0) {
                    context.resources.getText(R.string.reset)
                } else {
                    ('A' + i - 1).toString()
                }

                alpha = 0.6f
                setTextColor(context.resources.getColor(R.color.black))
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                buttonDrawable = null
                setOnClickListener {
                    getData(radioButton.text.toString())
                }

                layoutParams = params
            }
            mBinding.radioButton.addView(radioButton)
        }
    }

    private fun getData(data: String) {
        homeViewModel.buttonApiCall.postValue(Event(data))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(runnable)
    }
}