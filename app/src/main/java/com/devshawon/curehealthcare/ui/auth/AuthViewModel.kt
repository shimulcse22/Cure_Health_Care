package com.devshawon.curehealthcare.ui.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.devshawon.curehealthcare.network.Status
import com.devshawon.curehealthcare.models.LoginRequest
import com.devshawon.curehealthcare.models.LoginResponse
import com.devshawon.curehealthcare.models.RegistrationRequest
import com.devshawon.curehealthcare.models.RegistrationResponse
import com.devshawon.curehealthcare.network.Resource
import com.devshawon.curehealthcare.ui.repository.Repository
import com.devshawon.curehealthcare.useCase.TokenUseCase
import com.devshawon.curehealthcare.useCase.result.Event
import com.devshawon.curehealthcare.util.PreferenceStorage
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val repository: Repository,
    private val tokenUseCase: TokenUseCase,
    var preferences: PreferenceStorage,
) : ViewModel() {

    var phone = MutableLiveData<String>()
    var password = MutableLiveData<String>()

    var firstName = MutableLiveData<String>()
    var lastName = MutableLiveData<String>()
    var mobile = MutableLiveData<String>()
    var shopName = MutableLiveData<String>()
    var shopAddress = MutableLiveData<String>()
    var nid = MutableLiveData<String>()
    var license = MutableLiveData<String>()
    var loader = MutableLiveData<Boolean>()

    var loginRequest = MutableLiveData<LoginRequest>()
    var loginData = MutableLiveData<LoginResponse>()
    var loginResponse : LiveData<Resource<LoginResponse>> = loginRequest.switchMap {
        repository.login(it)
    }

    var regRequest = MutableLiveData<RegistrationRequest>()
    var regResponse : LiveData<Resource<RegistrationResponse>> = regRequest.switchMap {
        repository.registration(it)
    }


    val event = MutableLiveData<Event<String>>()

    init {
        loginResponse.observeForever {
            if(it.status == Status.SUCCESS && it.data != null){
                loader.value = false
                it.data.let {d->
                    loginData.value = d
                    preferences.mobileNumber = d.phone?:"233"
                    preferences.shopAddress = d.customer.shopName?:""
                    preferences.customerName = d.name?:""
                }
                it.data.token?.let { it1 -> tokenUseCase(it1) }
                event.postValue(Event(it.status.name))
            }else if(it.status == Status.ERROR){
                loader.value = false
                event.postValue(Event(it.error?.message!!))
            }
        }

        regResponse.observeForever {
            if(it.status == Status.SUCCESS && it.data !=null){
                loader.value = false
                event.postValue(Event(it.data.message?:""))
            }else if(it.status == Status.SUCCESS){
                loader.value = false
                event.postValue(Event("Can not created, Please contact with the helpline number"))
            }
        }
    }
}