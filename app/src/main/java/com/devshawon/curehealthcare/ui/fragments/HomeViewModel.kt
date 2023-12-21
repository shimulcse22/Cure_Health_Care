package com.devshawon.curehealthcare.ui.fragments

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.devshawon.curehealthcare.network.Status
import com.devshawon.curehealthcare.models.BannerResponseMobile
import com.devshawon.curehealthcare.models.Form
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

    var bannerRequest = MutableLiveData<Event<Unit>>()
    var bannerList = ArrayList<String>()
    var bannerListResponse : LiveData<Resource<BannerResponseMobile>> = bannerRequest.switchMap {
        repository.getBannerList()
    }

    var productRequest = MutableLiveData<Event<String>>()
    var productList = ArrayList<Form>()
    var productListResponse : LiveData<Resource<ProductResponse>> = productRequest.switchMap {
        repository.getProduct(it.peekContent())
    }

    var event = MutableLiveData<Event<String>>()
    var productEvent = MutableLiveData<Event<String>>()

    val buttonApiCall = MutableLiveData<Event<String>>()

    init {//event.postValue(Event(it.status.name))
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
                productList = it.data.forms as ArrayList<Form>
                productEvent.postValue(Event(it.status.name))
            }else if(it.status == Status.ERROR){
                productEvent.postValue(Event(it.status.name))
            }
        }
    }
}