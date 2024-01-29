package com.devshawon.curehealthcare.ui.fragments.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import java.lang.StringBuilder
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
            Log.d("THE PRODUCT REQUEST IS 53","${preferences.companyList} and ${preferences.formList}")
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = homeViewModel
        handler.removeCallbacks(runnable)
        runnable.run()

        addRadioButtons()

        mBinding.resetButton.textSize = 18f

        Log.d(
            "THE LIST IS ",
            "${(activity as CureHealthCareActivity).companyListLiveData} and ${(activity as CureHealthCareActivity).companyListLiveData}"
        )
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
//            (activity as CureHealthCareActivity).companyListLiveData.clear()
//            (activity as CureHealthCareActivity).formListLiveData.clear()
            preferences.radioData = ""
//            homeViewModel.productRequest.postValue(
//                Event(
//                    ProductRequest(
//                        company = returnString(preferences.companyList),
//                        firstLatter = preferences.radioData,
//                        form = returnString(preferences.formList),
//                        page = 1,
//                        search = ""
//                    )
//                )
//            )
            apiCall()
        }

       execute = { data, cList, fList ->
            if (data == "apply") {
                (activity as CureHealthCareActivity).productListActivity.clear()
//                val productRequest = ProductRequest(
//                    company = returnString(preferences.companyList),
//                    firstLatter = preferences.radioData,
//                    form = returnString(preferences.formList),
//                    page = 1,
//                    search = ""
//                )
//                homeViewModel.productRequest.postValue(
//                    Event(
//                        productRequest
//                    )
//                )
                apiCall()
            } else {
                (activity as CureHealthCareActivity).companyListLiveData.clear()
                (activity as CureHealthCareActivity).formListLiveData.clear()
                preferences.formList = mutableListOf()
                preferences.companyList = mutableListOf()
//                homeViewModel.productRequest.postValue(
//                    Event(
//                        ProductRequest(
//                            company = returnString(preferences.companyList),
//                            firstLatter = preferences.radioData,
//                            form = returnString(preferences.formList),
//                            page = 1,
//                            search = ""
//                        )
//                    )
//                )
                apiCall()
            }
        }
    }

    private fun addRadioButtons() {
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT
        )
        val myTextViews = arrayOfNulls<TextView>(26)
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
                    radioDataList.forEachIndexed { index, b ->
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
        super.onDestroyView()
        handler.removeCallbacks(runnable)
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

    companion object{
        var execute : (data: String, cList : ArrayList<String>, fList :ArrayList<String>) -> Unit = { data: String, sList: ArrayList<String>, fList :ArrayList<String>-> }
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
        Log.d("THE LIST IS STRING","${data}")
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