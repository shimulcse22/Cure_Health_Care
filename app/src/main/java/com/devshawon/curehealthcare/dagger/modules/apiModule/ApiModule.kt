package com.devshawon.curehealthcare.dagger.modules.apiModule

import com.devshawon.curehealthcare.api.ApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@Suppress("unused")
class ApiModule {

//    @Singleton
//    @Provides
//    fun provideBaseUrl(): HttpUrl {
//        return API_URL
//    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

//    @Singleton
//    @Provides
//    fun provideTokenInterceptor(
//        context: Context,
//        storage: PreferenceStorage,
//        tokenUseCase: TokenUseCase,
//        refreshTokenUseCase: RefreshTokenUseCase
//    ): TokenInterceptor {
//        return TokenInterceptor(context, storage, tokenUseCase, refreshTokenUseCase)
//    }

//    @Singleton
//    @Provides
//    fun provideOkHttpClient(tokenInterceptor: TokenInterceptor): OkHttpClient {
//        val mBuilder = OkHttpClient.Builder()
//            .readTimeout(300, TimeUnit.SECONDS)
//            .callTimeout(300, TimeUnit.SECONDS)
//            .writeTimeout(300, TimeUnit.SECONDS)
//        if (BuildConfig.DEBUG) mBuilder.addNetworkInterceptor(StethoInterceptor())
//        mBuilder.addInterceptor(tokenInterceptor)
//
//        return mBuilder.build()
//    }

//    @Singleton
//    @Provides
//    fun provideRetrofit(mBaseUrl: HttpUrl, mClient: OkHttpClient, mMoshi: Moshi): Retrofit {
//        return Retrofit.Builder()
//            .client(mClient)
//            .baseUrl(mBaseUrl)
//            .addConverterFactory(MoshiConverterFactory.create(mMoshi))
//            .addCallAdapterFactory(LiveDataCallAdapterFactory())
//            .build()
//    }

    @Singleton
    @Provides
    fun provideApiService(mRetrofit: Retrofit): ApiService {
        return mRetrofit.create(ApiService::class.java)
    }

    companion object {
        //var URL: String = BuildConfig.BASE_DOMAIN_URL
        //var API_URL: HttpUrl = HttpUrl.get(URL)
    }
}