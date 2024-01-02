package com.devshawon.curehealthcare.ui.fragments

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.devshawon.curehealthcare.network.Status
import com.devshawon.curehealthcare.models.BannerResponseMobile
import com.devshawon.curehealthcare.models.EditProfileData
import com.devshawon.curehealthcare.models.EditProfileGetRequest
import com.devshawon.curehealthcare.models.MedicinePhoto
import com.devshawon.curehealthcare.models.ProductData
import com.devshawon.curehealthcare.models.ProductForms
import com.devshawon.curehealthcare.models.ProductRequest
import com.devshawon.curehealthcare.models.ProductResponse
import com.devshawon.curehealthcare.models.UpdatePassword
import com.devshawon.curehealthcare.models.UpdatePasswordResponse
import com.devshawon.curehealthcare.models.UpdateProfileRequest
import com.devshawon.curehealthcare.models.UpdateProfileResponse
import com.devshawon.curehealthcare.network.Resource
import com.devshawon.curehealthcare.ui.repository.Repository
import com.devshawon.curehealthcare.useCase.result.Event
import com.devshawon.curehealthcare.util.PreferenceStorage
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val repository: Repository,
    var preferences: PreferenceStorage,
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

    var oldPassword = MutableLiveData<String>()
    var newPassword = MutableLiveData<String>()
    var confirmNewPassword = MutableLiveData<String>()

    var updatePasswordRequest = MutableLiveData<UpdatePassword>()
    var updatePasswordEvent = MutableLiveData<Event<String>>()
    var updatePasswordResponse : LiveData<Resource<UpdatePasswordResponse>> = updatePasswordRequest.switchMap {
        repository.updatePassword(it)
    }

    val editProfileEvent = MutableLiveData<Event<Unit>>()
    val editProfileResponse : LiveData<Resource<EditProfileGetRequest>> = editProfileEvent.switchMap {
        repository.getEditProfile()
    }

    val updateProfileRequest = MutableLiveData<UpdateProfileRequest>()
    val updateProfileEvent = MutableLiveData<Event<String>>()
    val updateProfileResponse : LiveData<Resource<UpdateProfileResponse>> = updateProfileRequest.switchMap {
        repository.updateProfile(it)
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

        updatePasswordResponse.observeForever {
            if(it.status == Status.SUCCESS && it.data != null){
                updatePasswordEvent.postValue(Event(it.data.message))
            }else if(it.status == Status.ERROR){
                updatePasswordEvent.postValue(Event(it.data?.message!!))
            }
        }

        editProfileResponse.observeForever {
            if(it.status == Status.SUCCESS && it.data != null){
                it.data.data?.let { d->
                    firstName.value = d.customer?.firstName?:""
                    lastName.value = d.customer?.lastName?:""
                    license.value = d.customer?.license?:""
                    nid.value = d.customer?.nid?:""
                    shopName.value = d.customer?.shopName?:""
                    shopAddress.value = d.customer?.shopAddress?:""
                    mobile.value = d.phone?:""
                }
            }else if(it.status == Status.ERROR){

            }
        }

        updateProfileResponse.observeForever {
            if(it.status == Status.SUCCESS && it.data != null){
                it.data.let { d->
                    firstName.value = d.customer?.firstName?:""
                    lastName.value = d.customer?.lastName?:""
                    license.value = d.customer?.license?:""
                    nid.value = d.customer?.nid?:""
                    shopName.value = d.customer?.shopName?:""
                    shopAddress.value = d.customer?.shopAddress?:""
                }
                updatePasswordEvent.postValue(Event(it.data.message?:"h"))
            }else if(it.status == Status.ERROR){
                updatePasswordEvent.postValue(Event(it.data?.message!!))
            }
        }
    }
}