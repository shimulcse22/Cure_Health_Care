package com.devshawon.curehealthcare.ui.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import bd.com.upay.customer.network.Status
import com.devshawon.curehealthcare.models.BannerResponseMobile
import com.devshawon.curehealthcare.network.Resource
import com.devshawon.curehealthcare.ui.repository.Repository
import com.devshawon.curehealthcare.useCase.result.Event
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    var bannerRequest = MutableLiveData<Event<Unit>>()
    var bannerList = ArrayList<String>()
    var bannerListResponse : LiveData<Resource<BannerResponseMobile>> = bannerRequest.switchMap {
        repository.getBannerList()
    }

    var event = MutableLiveData<Event<String>>()

    init {
        bannerListResponse.observeForever {
            if(it.status == Status.SUCCESS && it.data != null){
                bannerList = it.data.sliders as ArrayList<String>
                event.postValue(Event(it.status.name))
            }else if(it.status == Status.ERROR){
                event.postValue(Event(it.status.name))
            }
        }
    }
}