package com.devshawon.curehealthcare.ui.fragments

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.devshawon.curehealthcare.network.Status
import com.devshawon.curehealthcare.models.BannerResponseMobile
import com.devshawon.curehealthcare.models.MedicinePhoto
import com.devshawon.curehealthcare.models.ProductData
import com.devshawon.curehealthcare.models.ProductForms
import com.devshawon.curehealthcare.models.ProductRequest
import com.devshawon.curehealthcare.models.ProductResponse
import com.devshawon.curehealthcare.network.Resource
import com.devshawon.curehealthcare.ui.repository.Repository
import com.devshawon.curehealthcare.useCase.result.Event
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    var firstName = MutableLiveData<String>()
    var lastName = MutableLiveData<String>()
    var shopName = MutableLiveData<String>()
    var license = MutableLiveData<String>()
    var nid = MutableLiveData<String>()
    var mobile = MutableLiveData<String>()
    var shopAddress = MutableLiveData<String>()

//    val badge = MutableLiveData<Int>()
//    var productCount = MutableLiveData<t>()

    var bannerRequest = MutableLiveData<Event<Unit>>()
    var bannerList = ArrayList<String>()
    var bannerListResponse : LiveData<Resource<BannerResponseMobile>> = bannerRequest.switchMap {
        repository.getBannerList()
    }

    var productRequest = MutableLiveData<Event<ProductRequest>>()
    var productList = ArrayList<ProductData>()
    var productListLiveData = MutableLiveData<ArrayList<ProductData>>()
    var productListResponse : LiveData<Resource<ProductResponse>> = productRequest.switchMap {
        repository.getProduct(it.peekContent())
    }

    var trendingRequest = MutableLiveData<Event<Int>>()
    var trendingList = ArrayList<ProductData>()
    var trendingListResponse : LiveData<Resource<ProductResponse>> = trendingRequest.switchMap {
        repository.getTrending(it.peekContent())
    }

    var event = MutableLiveData<Event<String>>()
    var productEvent = MutableLiveData<Event<String>>()
    var productCount = MutableLiveData<Int>()

    val buttonApiCall = MutableLiveData<Event<String>>()

    init {
        bannerListResponse.observeForever {
            if(it.status == Status.SUCCESS && it.data != null){
                bannerList = it.data.sliders as ArrayList<String>
                event.postValue(Event(it.status.name))
            }else if(it.status == Status.ERROR){
                event.postValue(Event(it.status.name))
            }
        }

        productListResponse.observeForever {
            Log.d("THE DATA IS PRODUCT ","$it")
            if(it.status == Status.SUCCESS && it.data != null){
                productList = it.data.data as ArrayList<ProductData>
                productEvent.postValue(Event(it.status.name))
            }else if(it.status == Status.ERROR){
                productEvent.postValue(Event(it.status.name))
            }
        }

        trendingListResponse.observeForever {
            Log.d("THE DATA IS PRODUCT ","$it")
            if(it.status == Status.SUCCESS && it.data != null){
                trendingList = it.data.data as ArrayList<ProductData>
                productEvent.postValue(Event(it.status.name))
            }else if(it.status == Status.ERROR){
                productEvent.postValue(Event(it.status.name))
            }
        }
    }
}