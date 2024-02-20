package com.devshawon.curehealthcare.dagger.modules.apiModule

import android.content.Context
import com.devshawon.curehealthcare.api.ApiService
import com.devshawon.curehealthcare.network.adapter.LiveDataCallAdapterFactory
import com.devshawon.curehealthcare.network.token.TokenInterceptor
import com.devshawon.curehealthcare.useCase.RefreshTokenUseCase
import com.devshawon.curehealthcare.useCase.TokenUseCase
import com.devshawon.curehealthcare.util.PreferenceStorage
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@Suppress("unused")
class ApiModule {

    @Singleton
    @Provides
    fun provideBaseUrl(): HttpUrl {
        return API_URL
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    @Singleton
    @Provides
    fun provideTokenInterceptor(
        context: Context,
        storage: PreferenceStorage,
        tokenUseCase: TokenUseCase,
        refreshTokenUseCase: RefreshTokenUseCase
    ): TokenInterceptor {
        return TokenInterceptor(context, storage, tokenUseCase, refreshTokenUseCase)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(tokenInterceptor: TokenInterceptor): OkHttpClient {
        val mBuilder = OkHttpClient.Builder()
            .readTimeout(500, TimeUnit.SECONDS)
            .callTimeout(500, TimeUnit.SECONDS)
            .writeTimeout(500, TimeUnit.SECONDS)

        mBuilder.addInterceptor(tokenInterceptor)

        return mBuilder.build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(mBaseUrl: HttpUrl, mClient: OkHttpClient, mMoshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .client(mClient)
            .baseUrl(mBaseUrl)
            .addConverterFactory(MoshiConverterFactory.create(mMoshi))
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(mRetrofit: Retrofit): ApiService {
        return mRetrofit.create(ApiService::class.java)
    }

    companion object {
        //var URL: String = "https://stage.curehealth.care"
        var URL: String = "https://curehealth.care"
        var API_URL: HttpUrl = HttpUrl.get(URL)
    }
}