package com.devshawon.curehealthcare.api

import androidx.lifecycle.LiveData
import com.devshawon.curehealthcare.models.BannerResponseMobile
import com.devshawon.curehealthcare.models.LoginRequest
import com.devshawon.curehealthcare.models.LoginResponse
import com.devshawon.curehealthcare.models.RefreshToken
import com.devshawon.curehealthcare.models.RefreshTokenResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("api/v2/login")
    fun login(@Body data: LoginRequest): LiveData<ApiResponse<LoginResponse>>
    @POST("dfsc/oam/app/v1/login-refresh/")
    fun getRefreshToken(@Body data: RefreshToken): Call<RefreshTokenResponse>

    @GET("api/v2/mobile_slider")
    fun getBanner() : LiveData<ApiResponse<BannerResponseMobile>>
}