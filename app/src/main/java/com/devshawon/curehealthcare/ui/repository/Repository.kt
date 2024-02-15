package com.devshawon.curehealthcare.ui.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.devshawon.curehealthcare.api.ApiService
import com.devshawon.curehealthcare.api.AppExecutors
import com.devshawon.curehealthcare.models.BannerResponse
import com.devshawon.curehealthcare.models.BannerResponseMobile
import com.devshawon.curehealthcare.models.CancelOrderRequest
import com.devshawon.curehealthcare.models.CompanyResponse
import com.devshawon.curehealthcare.models.EditProfileGetRequest
import com.devshawon.curehealthcare.models.EmptyRequest
import com.devshawon.curehealthcare.models.ForgotPasswordRequest
import com.devshawon.curehealthcare.models.Form
import com.devshawon.curehealthcare.models.FormResponse
import com.devshawon.curehealthcare.models.LoginRequest
import com.devshawon.curehealthcare.models.LoginResponse
import com.devshawon.curehealthcare.models.NotificationResponse
import com.devshawon.curehealthcare.models.OrderResponse
import com.devshawon.curehealthcare.models.PlaceOrderRequest
import com.devshawon.curehealthcare.models.PlaceOrderResponse
import com.devshawon.curehealthcare.models.ProductRequest
import com.devshawon.curehealthcare.models.ProductResponse
import com.devshawon.curehealthcare.models.RegistrationRequest
import com.devshawon.curehealthcare.models.RegistrationResponse
import com.devshawon.curehealthcare.models.SingleOrderResponse
import com.devshawon.curehealthcare.models.UpdatePassword
import com.devshawon.curehealthcare.models.UpdatePasswordResponse
import com.devshawon.curehealthcare.models.UpdateProfileRequest
import com.devshawon.curehealthcare.models.UpdateProfileResponse
import com.devshawon.curehealthcare.network.NetworkBoundResourceOnlyNetwork
import com.devshawon.curehealthcare.network.Resource
import com.devshawon.curehealthcare.util.getInt
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
    fun registration(data: RegistrationRequest): LiveData<Resource<RegistrationResponse>> {
        return object : NetworkBoundResourceOnlyNetwork<RegistrationResponse>(appExecutors) {
            override fun createCall() = apiService.registration(data)
        }.asLiveData()
    }

    fun getBannerList(): LiveData<Resource<BannerResponseMobile>> {
        return object : NetworkBoundResourceOnlyNetwork<BannerResponseMobile>(appExecutors) {
            override fun createCall() = apiService.getBanner()
        }.asLiveData()
    }

    fun getProduct(search : ProductRequest) : LiveData<Resource<ProductResponse>>{
        Log.d("THE PRODUCT REQUEST IS","$search")
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

    fun getOrder(page: String?="") : LiveData<Resource<OrderResponse>>{
        Log.d("Order is called","")
        return object : NetworkBoundResourceOnlyNetwork<OrderResponse>(appExecutors) {
            override fun createCall() = apiService.getOrder(getInt(page))
        }.asLiveData()
    }

    fun getNotifications(page: Int?=0) : LiveData<Resource<NotificationResponse>>{
        return object : NetworkBoundResourceOnlyNetwork<NotificationResponse>(appExecutors) {
            override fun createCall() = apiService.getNotification(page)
        }.asLiveData()
    }

    fun postPlaceOrder(body : PlaceOrderRequest) : LiveData<Resource<PlaceOrderResponse>>{
        return object : NetworkBoundResourceOnlyNetwork<PlaceOrderResponse>(appExecutors) {
            override fun createCall() = apiService.postPlaceOrder(body)
        }.asLiveData()
    }

    fun getSingleOrder(id : Int?=0) : LiveData<Resource<SingleOrderResponse>>{
        return object : NetworkBoundResourceOnlyNetwork<SingleOrderResponse>(appExecutors) {
            override fun createCall() = apiService.getSingleOrder(id)
        }.asLiveData()
    }

    fun cancelOrder(body: CancelOrderRequest) : LiveData<Resource<RegistrationResponse>>{
        return object : NetworkBoundResourceOnlyNetwork<RegistrationResponse>(appExecutors) {
            override fun createCall() = apiService.cancelOrder(body)
        }.asLiveData()
    }

    fun markAsReadOrder(body: EmptyRequest) : LiveData<Resource<RegistrationResponse>>{
        return object : NetworkBoundResourceOnlyNetwork<RegistrationResponse>(appExecutors) {
            override fun createCall() = apiService.markAsRead()
        }.asLiveData()
    }

    fun forgotPassword(body: ForgotPasswordRequest) : LiveData<Resource<RegistrationResponse>>{
        return object : NetworkBoundResourceOnlyNetwork<RegistrationResponse>(appExecutors) {
            override fun createCall() = apiService.forgotPassword(body)
        }.asLiveData()
    }

    fun versionCheck() : LiveData<Resource<RegistrationResponse>>{
        Log.d("Version Check 1","")
        return object : NetworkBoundResourceOnlyNetwork<RegistrationResponse>(appExecutors) {
            override fun createCall() = apiService.version()
        }.asLiveData()
    }
}