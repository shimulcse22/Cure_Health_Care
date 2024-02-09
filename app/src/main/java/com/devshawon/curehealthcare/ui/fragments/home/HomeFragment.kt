package com.devshawon.curehealthcare.ui.fragments.home

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
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
import com.devshawon.curehealthcare.ui.fragments.filter.FilterFragment
import com.devshawon.curehealthcare.useCase.result.Event
import com.devshawon.curehealthcare.useCase.result.EventObserver
import com.devshawon.curehealthcare.util.navigate
import kotlinx.coroutines.launch
import ru.nikartm.support.BadgePosition
import javax.inject.Inject


class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home), OnItemClick {
    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val homeViewModel: HomeViewModel by navGraphViewModels(R.id.cure_health_care_nav_host_xml) { viewModelFactory }


    private lateinit var homeBannerAdapter: CommonAdapter
    private lateinit var productAdapter: ProductAdapter

    private val handler = Handler(Looper.getMainLooper())
    private var interval = 0
    private var currentPosition = 0

    private val radioDataList = ArrayList<Boolean>()

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
        //lifecycleScope.launch {
        homeViewModel.productRequest.postValue(
            Event(
                ProductRequest(
                    company = returnString(preferences.companyList),
                    firstLatter = preferences.radioData,
                    form = returnString(preferences.formList),
                    page = homeViewModel.pageCount.value ?: (0 + 1),
                    search = ""
                )
            )
        )
        //}

        lifecycleScope.launch {
            for (i in 0 until 26) {
                radioDataList.add(false)
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = homeViewModel
        handler.removeCallbacks(runnable)
        runnable.run()

        homeViewModel.notificationRequest.postValue(Event(0))

        val myTextViews = arrayOfNulls<TextView?>(26)
        addRadioButtons(myTextViews)

        mBinding.resetButton.textSize = 18f

        mBinding.filterImage.apply {
            if(preferences.companyList?.isNotEmpty()!! || preferences.formList?.isNotEmpty()!!){
                badgeValue = preferences.companyList?.size!! + preferences.formList?.size!!
                isBadgeOvalAfterFirst = true
                badgeTextSize = 10f
                maxBadgeValue = 99
                badgePosition = BadgePosition.TOP_RIGHT
                badgeTextStyle = Typeface.NORMAL
                isShowCounter = true
                setBadgePadding(5)
            }else{
                badgeValue = 0
            }
        }
        mBinding.alarmImage.apply {
            badgeValue = homeViewModel.notificationCount
            isBadgeOvalAfterFirst = true
            badgeTextSize = 10f
            maxBadgeValue = 99
            badgePosition = BadgePosition.TOP_RIGHT
            badgeTextStyle = Typeface.NORMAL
            isShowCounter = true
            setBadgePadding(5)
        }

        mBinding.bannerRecyclerView.adapter = homeBannerAdapter
        mBinding.bannerRecyclerView.itemAnimator = DefaultItemAnimator()
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
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
            } else {
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
                if (!recyclerView.canScrollVertically(1) && !homeViewModel.nextPage.value.isNullOrEmpty()) {
                    homeViewModel.productRequest.postValue(
                        Event(
                            ProductRequest(
                                company = returnString(preferences.companyList),
                                firstLatter = preferences.radioData,
                                form = returnString(preferences.formList),
                                page = (homeViewModel.pageCount.value!! + 1),
                                search = ""
                            )
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
            (activity as CureHealthCareActivity).companyListLiveData.clear()
            (activity as CureHealthCareActivity).formListLiveData.clear()
            preferences.radioData = ""
            preferences.formList = mutableListOf()
            preferences.companyList = mutableListOf()
            mBinding.filterImage.badgeValue = 0

            homeViewModel.productRequest.postValue(
                Event(
                    ProductRequest(
                        company = returnString((activity as CureHealthCareActivity).companyListLiveData),
                        firstLatter = preferences.radioData,
                        form = returnString((activity as CureHealthCareActivity).formListLiveData),
                        page = 1,
                        search = ""
                    )
                )
            )
            radioDataList.forEachIndexed { index, _ ->
                if ( radioDataList[index]) {
                    radioDataList[index] = false
                    myTextViews[index]?.setBackgroundResource(R.drawable.rect_angle_shape_white)
                    myTextViews[index]?.setTextColor(resources.getColor(R.color.black))
                }
            }
        }

        FilterFragment.execute = { data, _, _ ->
            if (data == "apply") {
                (activity as CureHealthCareActivity).productListActivity.clear()
                (activity as CureHealthCareActivity).formListLiveData.clear()
                (activity as CureHealthCareActivity).companyListLiveData.clear()
                val cList = ArrayList<String>()
                val fList = ArrayList<String>()
                homeViewModel.companyList.forEach {
                    if(it.checkBox == true){
                        (activity as CureHealthCareActivity).companyListLiveData.add(it.id.toString())
                        cList.add(it.id.toString())
                    }
                }

                preferences.companyList = cList.toMutableList()

                homeViewModel.formList.forEach {
                    if(it.checkBox == true){
                        (activity as CureHealthCareActivity).formListLiveData.add(it.id.toString())
                        fList.add(it.id.toString())
                    }
                }

                preferences.formList = fList.toMutableList()

                apiCall()
            } else {
                (activity as CureHealthCareActivity).companyListLiveData.clear()
                (activity as CureHealthCareActivity).formListLiveData.clear()
                preferences.formList = mutableListOf()
                preferences.companyList = mutableListOf()
                apiCall()
            }
        }

    }

    @SuppressLint("SetTextI18n")
    private fun addRadioButtons(myTextViews :Array<TextView?>) {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
        )

        for (i in 0 until 26) {
            val rowTextView = TextView(requireContext())
            rowTextView.text = "${('A' + i)}"
            rowTextView.textSize = 18f
            rowTextView.setPadding(20, 10, 20, 10)
            params.setMargins(8, 0, 8, 0)
            if(preferences.radioData == "${('A' + i)}"){
                rowTextView.setBackgroundResource(R.drawable.rect_angle_shape_black)
                rowTextView.setTextColor(resources.getColor(R.color.white))
                radioDataList[i] = true
            }else{
                rowTextView.setBackgroundResource(R.drawable.rect_angle_shape_white)
                rowTextView.setTextColor(resources.getColor(R.color.black))
            }


            mBinding.radioButton.addView(rowTextView)
            rowTextView.setOnClickListener {
                if (!radioDataList[i]) {
                    rowTextView.setBackgroundResource(R.drawable.rect_angle_shape_black)
                    rowTextView.setTextColor(resources.getColor(R.color.white))
                    radioDataList[i] = true
                    radioDataList.forEachIndexed { index, _ ->
                        if (i != index && radioDataList[index]) {
                            radioDataList[index] = false
                            myTextViews[index]?.setBackgroundResource(R.drawable.rect_angle_shape_white)
                            myTextViews[index]?.setTextColor(resources.getColor(R.color.black))
                        }
                    }
                    getData("${('A' + i)}")
                } else {
                    rowTextView.setBackgroundResource(R.drawable.rect_angle_shape_white)
                    rowTextView.setTextColor(resources.getColor(R.color.black))
                    radioDataList[i] = false
                    getData("")
                }
            }
            myTextViews[i] = rowTextView
            rowTextView.layoutParams = params
        }
    }

    private fun getData(data: String) {
        homeViewModel.pageCount.value = 1
        if (data.isEmpty()) (activity as CureHealthCareActivity).productListActivity = arrayListOf()
        //productAdapter.productList.clear()
        preferences.radioData = data
        //move to another function
        apiCall()
    }

    override fun onDestroyView() {
        handler.removeCallbacks(runnable)
        super.onDestroyView()
    }

    override fun onPlusIconClick(item: ProductData) {
        homeViewModel.productCount.value = homeViewModel.productCount.value ?: (0 + 1)
        (activity as UpdateCart).inCreaseItem(item)
        (activity as CureHealthCareActivity).showOrHideBadge()
    }

    override fun onMinusIconClick(item: ProductData, position: Int,isDelete : Boolean) {
        homeViewModel.productCount.value = homeViewModel.productCount.value ?: (0 - 1)
        (activity as UpdateCart).decreaseItem(item, position,isDelete)
        (activity as CureHealthCareActivity).showOrHideBadge()
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



    private fun returnString( list : MutableList<String?>?) : String{
        if(list?.isEmpty()==true) return  ""
        val data = StringBuilder()
        list?.forEachIndexed { id, d->
            if(id == list.size - 1){
                data.append(d)
            }else{
                data.append(d).append(",")
            }

        }
        return data.toString()
    }

    private fun apiCall(){
        homeViewModel.productRequest.postValue(
            Event(
                ProductRequest(
                    company = returnString((activity as CureHealthCareActivity).companyListLiveData),
                    firstLatter = preferences.radioData,
                    form = returnString((activity as CureHealthCareActivity).formListLiveData),
                    page = 1,
                    search = ""
                )
            )
        )
    }
}