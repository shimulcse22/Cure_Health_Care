package com.devshawon.curehealthcare.ui.repository

import androidx.lifecycle.LiveData
import com.devshawon.curehealthcare.api.ApiService
import com.devshawon.curehealthcare.api.AppExecutors
import com.devshawon.curehealthcare.models.BannerResponse
import com.devshawon.curehealthcare.models.BannerResponseMobile
import com.devshawon.curehealthcare.models.CompanyResponse
import com.devshawon.curehealthcare.models.EditProfileGetRequest
import com.devshawon.curehealthcare.models.Form
import com.devshawon.curehealthcare.models.FormResponse
import com.devshawon.curehealthcare.models.LoginRequest
import com.devshawon.curehealthcare.models.LoginResponse
import com.devshawon.curehealthcare.models.ProductRequest
import com.devshawon.curehealthcare.models.ProductResponse
import com.devshawon.curehealthcare.models.UpdatePassword
import com.devshawon.curehealthcare.models.UpdatePasswordResponse
import com.devshawon.curehealthcare.models.UpdateProfileRequest
import com.devshawon.curehealthcare.models.UpdateProfileResponse
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

    fun getProduct(search : ProductRequest) : LiveData<Resource<ProductResponse>>{
        return object : NetworkBoundResourceOnlyNetwork<ProductResponse>(appExecutors) {
            override fun createCall() = apiService.getProduct(search)
        }.asLiveData()
    }

    fun getTrending(page : Int) : LiveData<Resource<ProductResponse>>{
        return object : NetworkBoundResourceOnlyNetwork<ProductResponse>(appExecutors) {
            override fun createCall() = apiService.getTrending(page)
        }.asLiveData()
    }

    fun updatePassword(body : UpdatePassword) : LiveData<Resource<UpdatePasswordResponse>>{
        return object : NetworkBoundResourceOnlyNetwork<UpdatePasswordResponse>(appExecutors) {
            override fun createCall() = apiService.updatePassword(body)
        }.asLiveData()
    }

    fun getEditProfile() : LiveData<Resource<EditProfileGetRequest>>{
        return object : NetworkBoundResourceOnlyNetwork<EditProfileGetRequest>(appExecutors) {
            override fun createCall() = apiService.getProfile()
        }.asLiveData()
    }

    fun updateProfile(body : UpdateProfileRequest) : LiveData<Resource<UpdateProfileResponse>>{
        return object : NetworkBoundResourceOnlyNetwork<UpdateProfileResponse>(appExecutors) {
            override fun createCall() = apiService.updateProfile(body)
        }.asLiveData()
    }

    fun getCompany(search: String?="") : LiveData<Resource<CompanyResponse>>{
        return object : NetworkBoundResourceOnlyNetwork<CompanyResponse>(appExecutors) {
            override fun createCall() = apiService.getCompany(search)
        }.asLiveData()
    }

    fun getForm(search: String?="") : LiveData<Resource<FormResponse>>{
        return object : NetworkBoundResourceOnlyNetwork<FormResponse>(appExecutors) {
            override fun createCall() = apiService.getForm(search)
        }.asLiveData()
    }
}