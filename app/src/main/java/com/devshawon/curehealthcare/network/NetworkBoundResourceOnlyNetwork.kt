package com.devshawon.curehealthcare.network

import android.util.Log
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.devshawon.curehealthcare.api.ApiEmptyResponse
import com.devshawon.curehealthcare.api.ApiErrorResponse
import com.devshawon.curehealthcare.api.ApiResponse
import com.devshawon.curehealthcare.api.ApiSuccessResponse
import com.devshawon.curehealthcare.api.AppExecutors

abstract class NetworkBoundResourceOnlyNetwork<ResultType>
@MainThread constructor(private val appExecutors: AppExecutors) {

    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        setValue(Resource.loading(null))
        fetchFromNetwork()
    }

    private fun fetchFromNetwork() {
        val apiResponse = createCall()
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            when (response) {
                is ApiSuccessResponse -> {
                    appExecutors.diskIO().execute {
                        val data = processResponse(response)
                        this.save(Resource.success(data))
                        appExecutors.mainThread().execute {
                            setValue(Resource.success(data))
                        }
                    }
                }
                is ApiEmptyResponse -> {
                    appExecutors.mainThread().execute {
                        setValue(Resource.success(null))
                    }
                }
                is ApiErrorResponse -> {
                    appExecutors.mainThread().execute {
                        onFetchFailed(response)
                        setValue(Resource.error(response.error, null))
                    }
                }
                else -> {}
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun onFetchFailed(response: ApiErrorResponse<ResultType>) {
        Log.d("THE DATA IS 89",response.error.code)
    }

    fun asLiveData() = result as LiveData<Resource<ResultType>>
    fun asResponse() = result

    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<ResultType>) = response.body

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<ResultType>>

    @WorkerThread
    protected open fun save(response: Resource<ResultType>) {
    }
}
