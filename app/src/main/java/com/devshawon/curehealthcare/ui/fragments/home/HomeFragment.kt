package com.devshawon.curehealthcare.ui.fragments.home

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toolbar.LayoutParams
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.SnapHelper
import com.devshawon.curehealthcare.R
import com.devshawon.curehealthcare.base.ui.BaseFragment
import com.devshawon.curehealthcare.dagger.viewModel.AppViewModelFactory
import com.devshawon.curehealthcare.databinding.FragmentHomeBinding
import com.devshawon.curehealthcare.models.ProductData
import com.devshawon.curehealthcare.models.ProductRequest
import com.devshawon.curehealthcare.network.Status
import com.devshawon.curehealthcare.ui.CureHealthCareActivity
import com.devshawon.curehealthcare.ui.adapter.CommonAdapter
import com.devshawon.curehealthcare.ui.adapter.ProductAdapter
import com.devshawon.curehealthcare.ui.fragments.HomeViewModel
import com.devshawon.curehealthcare.ui.fragments.OnItemClick
import com.devshawon.curehealthcare.ui.fragments.UpdateCart
import com.devshawon.curehealthcare.useCase.result.Event
import com.devshawon.curehealthcare.useCase.result.EventObserver
import com.devshawon.curehealthcare.util.navigate
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.floor

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home), OnItemClick {
    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val homeViewModel: HomeViewModel by navGraphViewModels(R.id.cure_health_care_nav_host_xml) { viewModelFactory }


    private lateinit var homeBannerAdapter: CommonAdapter
    private lateinit var productAdapter: ProductAdapter

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
        homeViewModel.resetData()
        homeBannerAdapter = CommonAdapter()
        productAdapter = ProductAdapter(onItemClick = this)
        lifecycleScope.launch {
            homeViewModel.bannerRequest.postValue(Event(Unit))
        }
        lifecycleScope.launch {
            homeBannerAdapter.updateContext(requireContext())
            productAdapter.updateContext(requireContext())
        }
        lifecycleScope.launch {
            homeViewModel.productRequest.postValue(
                Event(
                    ProductRequest(
                        company = "", firstLatter = "", form = "", page = 1, search = ""
                    )
                )
            )
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

        mBinding.medicineLayout.adapter = productAdapter
        mBinding.medicineLayout.itemAnimator = DefaultItemAnimator()
        mBinding.medicineLayout.layoutManager = WrapContentLinearLayoutManager()

        if (mBinding.bannerRecyclerView.onFlingListener == null) {
            snapHelper.attachToRecyclerView(mBinding.bannerRecyclerView)
        }

        homeViewModel.event.observe(viewLifecycleOwner, EventObserver {
            if (it == Status.SUCCESS.name) {
                interval = (3 * 1000)
                handler.postDelayed(runnable, interval.toLong())
                homeBannerAdapter.updateList(homeViewModel.bannerList, 0)
            }
        })

        homeViewModel.productEvent.observe(viewLifecycleOwner, EventObserver {
            (activity as CureHealthCareActivity).productListActivity.addAll(homeViewModel.productList)
            if (it == Status.SUCCESS.name && homeViewModel.pageCount.value == 1) {
                productAdapter.updateProductList(homeViewModel.productList, 1)
            }
            else{
                productAdapter.addProductList(homeViewModel.productList)
            }
        })

        mBinding.searchImage.setOnClickListener {
            navigate(HomeFragmentDirections.actionHomeToSearchFragment())
        }

        mBinding.alarmImage.setOnClickListener {
            navigate(HomeFragmentDirections.actionHomeToNotificationFragment())
        }

        mBinding.bannerRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val offset: Int = mBinding.bannerRecyclerView.computeHorizontalScrollOffset()
                if (offset % mBinding.bannerRecyclerView.width == 0) {
                    currentPosition = offset / mBinding.bannerRecyclerView.width
                }
            }
        })

        mBinding.medicineLayout.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1)) {
                    val request = ProductRequest(
                        company = "", firstLatter = "", form = "", page = (homeViewModel.pageCount.value!!+1) , search = ""
                    )
                    homeViewModel.productRequest.postValue(
                        Event(
                            request
                        )
                    )
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        mBinding.filterImage.setOnClickListener {
            navigate(HomeFragmentDirections.actionHomeToFilterFragment())
        }

        mBinding.resetButton.setOnClickListener {
            homeViewModel.productRequest.postValue(
                Event(
                    ProductRequest(
                        company = "", firstLatter = "", form = "", page = 1, search = ""
                    )
                )
            )
        }
    }

    private fun addRadioButtons() {
        mBinding.radioButton.orientation = LinearLayout.HORIZONTAL
        val params = RadioGroup.LayoutParams(
            RadioGroup.LayoutParams.MATCH_PARENT,
            RadioGroup.LayoutParams.MATCH_PARENT
        )
        for (i in 0 until 26) {
            val radioButton = RadioButton(requireContext())
            radioButton.apply {
                id = i
                params.setMargins(8, 0, 8, 0)
                setBackgroundResource(R.drawable.radio_button_background)
                //textSize = 17f
                setPadding(15, 10, 15, 10)
                text = ('A' + i).toString()
                //setTextColor(ContextCompat.getColorStateList(context, R.color.text_color_checked))
                buttonDrawable = null
                textAlignment = View.TEXT_ALIGNMENT_CENTER
                setOnClickListener {
                    getData(radioButton.text.toString())
                }
                setTextAppearance(R.style.radio)
                layoutParams = params
            }
            mBinding.radioButton.addView(radioButton)
        }
    }

    private fun getData(data: String) {
        //homeViewModel.buttonApiCall.postValue(Event(data))
        homeViewModel.productRequest.postValue(
            Event(
                ProductRequest(
                    company = "", firstLatter = data, form = "", page = 1, search = ""
                )
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(runnable)
    }

    override fun onPlusIconClick(item: ProductData) {
        homeViewModel.productCount.value = homeViewModel.productCount.value ?: (0 + 1)
        (activity as UpdateCart).inCreaseItem(item)
        (activity as CureHealthCareActivity).showOrHideBadge(0)
    }

    override fun onMinusIconClick(item: ProductData,position:Int) {
        homeViewModel.productCount.value = homeViewModel.productCount.value ?: (0 - 1)
        (activity as UpdateCart).decreaseItem(item,position)
        (activity as CureHealthCareActivity).showOrHideBadge(0)
    }

    inner class WrapContentLinearLayoutManager : LinearLayoutManager(requireContext(),VERTICAL,false) {
        override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
            try {
                super.onLayoutChildren(recycler, state)
            } catch (_: IndexOutOfBoundsException) {

            }
        }
    }
}