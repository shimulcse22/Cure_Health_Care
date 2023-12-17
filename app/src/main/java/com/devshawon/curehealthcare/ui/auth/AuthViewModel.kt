package com.devshawon.curehealthcare.ui.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.devshawon.curehealthcare.network.Status
import com.devshawon.curehealthcare.models.LoginRequest
import com.devshawon.curehealthcare.models.LoginResponse
import com.devshawon.curehealthcare.network.Resource
import com.devshawon.curehealthcare.ui.repository.Repository
import com.devshawon.curehealthcare.useCase.TokenUseCase
import com.devshawon.curehealthcare.useCase.result.Event
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val repository: Repository,
    private val tokenUseCase: TokenUseCase,
) : ViewModel() {

    var phone = MutableLiveData<String>()
    var password = MutableLiveData<String>()

    var loginRequest = MutableLiveData<LoginRequest>()
    var loginData = MutableLiveData<LoginResponse>()
    var loginResponse : LiveData<Resource<LoginResponse>> = loginRequest.switchMap {
        repository.login(it)
    }

    var event = MutableLiveData<Event<String>>()

    init {
        loginResponse.observeForever {
            if(it.status == Status.SUCCESS && it.data != null){
                Log.d("THE DATA IS 1",it.status.name)
                it.data.let {d->
                    loginData.value = d
                }
                it.data.token?.let { it1 -> tokenUseCase(it1) }

                event.postValue(Event(it.status.name))
            }else if(it.status == Status.ERROR){
                Log.d("THE DATA IS 2",it.status.name)

                event.postValue(Event(it.status.name))
            }
        }
    }
}