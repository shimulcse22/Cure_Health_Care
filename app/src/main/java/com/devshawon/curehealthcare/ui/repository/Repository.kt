package com.devshawon.curehealthcare.ui.repository

import androidx.lifecycle.LiveData
import com.devshawon.curehealthcare.api.ApiService
import com.devshawon.curehealthcare.api.AppExecutors
import com.devshawon.curehealthcare.models.BannerResponse
import com.devshawon.curehealthcare.models.BannerResponseMobile
import com.devshawon.curehealthcare.models.LoginRequest
import com.devshawon.curehealthcare.models.LoginResponse
import com.devshawon.curehealthcare.models.ProductResponse
import com.devshawon.curehealthcare.network.NetworkBoundResourceOnlyNetwork
import com.devshawon.curehealthcare.network.Resource
import javax.inject.Inject

class Repository @Inject constructor(
    val appExecutors: AppExecutors,
    val apiService: ApiService
)  {
    fun login(data: LoginRequest): LiveData<Resource<LoginResponse>> {
        return object : NetworkBoundResourceOnlyNetwork<LoginResponse>(appExecutors) {
            override fun createCall() = apiService.login(data)
        }.asLiveData()
    }
    fun getBannerList(): LiveData<Resource<BannerResponseMobile>> {
        return object : NetworkBoundResourceOnlyNetwork<BannerResponseMobile>(appExecutors) {
            override fun createCall() = apiService.getBanner()
        }.asLiveData()
    }

    fun getProduct(search : String?="tab") : LiveData<Resource<ProductResponse>>{
        return object : NetworkBoundResourceOnlyNetwork<ProductResponse>(appExecutors) {
            override fun createCall() = apiService.getProduct(search?:"tab")
        }.asLiveData()
    }
}