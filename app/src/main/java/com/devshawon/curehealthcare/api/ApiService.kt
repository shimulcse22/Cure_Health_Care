package com.devshawon.curehealthcare.api

import androidx.lifecycle.LiveData
import com.devshawon.curehealthcare.models.BannerResponseMobile
import com.devshawon.curehealthcare.models.CompanyResponse
import com.devshawon.curehealthcare.models.EditProfileData
import com.devshawon.curehealthcare.models.EditProfileGetRequest
import com.devshawon.curehealthcare.models.FormResponse
import com.devshawon.curehealthcare.models.LoginRequest
import com.devshawon.curehealthcare.models.LoginResponse
import com.devshawon.curehealthcare.models.NotificationResponse
import com.devshawon.curehealthcare.models.OrderResponse
import com.devshawon.curehealthcare.models.PlaceOrderRequest
import com.devshawon.curehealthcare.models.PlaceOrderResponse
import com.devshawon.curehealthcare.models.ProductRequest
import com.devshawon.curehealthcare.models.ProductResponse
import com.devshawon.curehealthcare.models.RefreshToken
import com.devshawon.curehealthcare.models.RefreshTokenResponse
import com.devshawon.curehealthcare.models.RegistrationRequest
import com.devshawon.curehealthcare.models.RegistrationResponse
import com.devshawon.curehealthcare.models.UpdatePassword
import com.devshawon.curehealthcare.models.UpdatePasswordResponse
import com.devshawon.curehealthcare.models.UpdateProfileRequest
import com.devshawon.curehealthcare.models.UpdateProfileResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ApiService {

    @POST("api/v2/login")
    fun login(@Body data: LoginRequest): LiveData<ApiResponse<LoginResponse>>
    @POST("dfsc/oam/app/v1/login-refresh/")
    fun getRefreshToken(@Body data: RefreshToken): Call<RefreshTokenResponse>

    @GET("api/v2/mobile_slider")
    fun getBanner() : LiveData<ApiResponse<BannerResponseMobile>>

    @POST("api/v2/products")
    fun getProduct(@Body body : ProductRequest) : LiveData<ApiResponse<ProductResponse>>

    @GET("api/v2/trending")
    fun getTrending( @Query("page") page: Int?=1) : LiveData<ApiResponse<ProductResponse>>

    @PUT("api/v2/profile/password-reset")
    fun updatePassword(@Body body : UpdatePassword) : LiveData<ApiResponse<UpdatePasswordResponse>>

    @GET("api/v2/profile_info")
    fun getProfile() : LiveData<ApiResponse<EditProfileGetRequest>>

    @PUT("api/v2/profile/update")
    fun updateProfile(@Body body : UpdateProfileRequest) : LiveData<ApiResponse<UpdateProfileResponse>>

    @GET("api/v2/mobile_company")
    fun getCompany( @Query("search") search: String?="") : LiveData<ApiResponse<CompanyResponse>>

    @GET("api/v2/mobile_forms")
    fun getForm( @Query("search") search: String?="") : LiveData<ApiResponse<FormResponse>>

    @GET("api/v2/orders")
    fun getOrder( @Query("page") search: Int?=0) : LiveData<ApiResponse<OrderResponse>>

    @GET("api/v2/notifications")
    fun getNotification( @Query("page") search: Int?=0) : LiveData<ApiResponse<NotificationResponse>>

    @POST("api/v2/registration")
    fun registration( @Body body : RegistrationRequest) : LiveData<ApiResponse<RegistrationResponse>>

    @POST("api/v2/app_checkout")
    fun postPlaceOrder( @Body body : PlaceOrderRequest) : LiveData<ApiResponse<PlaceOrderResponse>>
}