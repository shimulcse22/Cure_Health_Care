package com.devshawon.curehealthcare.network.token

import android.content.Context
import com.devshawon.curehealthcare.api.ApiService
import com.devshawon.curehealthcare.models.RefreshToken
import com.devshawon.curehealthcare.useCase.RefreshTokenUseCase
import com.devshawon.curehealthcare.useCase.TokenUseCase
import com.devshawon.curehealthcare.util.PreferenceStorage
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

class TokenInterceptor constructor(
    private val context: Context,
    private val storage: PreferenceStorage,
    private val tokenUseCase: TokenUseCase,
    private val refreshTokenUseCase: RefreshTokenUseCase
) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        synchronized(this) {
            val request = request(chain, storage.token)
            val initialResponse = chain.proceed(request)
            val code = initialResponse.code()
            Timber.d(code.toString())
            when {
                code == 423 -> {
                    logout()
                    return initialResponse
                }
                code == 403 && storage.token.isNotEmpty() -> {
                    val tokenResponse = runBlocking {
                        val build = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
                        val client = OkHttpClient.Builder()
                            .addNetworkInterceptor(StethoInterceptor()).build()
                        val retrofit = Retrofit.Builder()
                            .client(client)
                            .baseUrl("API_URL")
                            .addConverterFactory(MoshiConverterFactory.create(build)).build()
                        val apiService = retrofit.create(ApiService::class.java)
                        val data = RefreshToken(storage.refreshToken)
                        apiService.getRefreshToken(data).execute()
                    }

                    return when {
                        tokenResponse.code() != 200 -> {
                            logout()
                            initialResponse
                        }
                        else -> {

                            val refreshTokenResponse = tokenResponse.body()
                            return if (refreshTokenResponse == null) {
                                logout()
                                initialResponse
                            } else {
                                val token = refreshTokenResponse.data?.accessToken
                                val refreshToken = refreshTokenResponse.data?.refreshToken
                                tokenUseCase(token!!)
                                refreshTokenUseCase(refreshToken!!)
                                initialResponse.close()
                                val newRequest = request(chain, token)
                                chain.proceed(newRequest)
                                // initialResponse
                            }
                        }
                    }
                }
                else -> {
                    return initialResponse
                }
            }
        }
    }

    private fun request(chain: Interceptor.Chain, token: String): Request {
        val original = chain.request()
        val originalHttpUrl = original.url()
        val appName = "customer"


        val requestBuilder = original.newBuilder()
            .addHeader("E-App-Name", appName)
            .url(originalHttpUrl)
        return requestBuilder.build()
    }

    private fun logout() {
        Timber.e("Logout-")
        //val intent = Intent(ACTION_LOGOUT)
        //LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }
}

