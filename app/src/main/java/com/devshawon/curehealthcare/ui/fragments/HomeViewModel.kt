package com.devshawon.curehealthcare.ui.fragments

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.devshawon.curehealthcare.models.BannerResponseMobile
import com.devshawon.curehealthcare.models.CancelOrderRequest
import com.devshawon.curehealthcare.models.CompanyResponse
import com.devshawon.curehealthcare.models.EditProfileGetRequest
import com.devshawon.curehealthcare.models.EmptyRequest
import com.devshawon.curehealthcare.models.Form
import com.devshawon.curehealthcare.models.FormResponse
import com.devshawon.curehealthcare.models.NotificationResponse
import com.devshawon.curehealthcare.models.NotificationResponseData
import com.devshawon.curehealthcare.models.OrderData
import com.devshawon.curehealthcare.models.OrderResponse
import com.devshawon.curehealthcare.models.PlaceOrderRequest
import com.devshawon.curehealthcare.models.PlaceOrderResponse
import com.devshawon.curehealthcare.models.ProductData
import com.devshawon.curehealthcare.models.ProductRequest
import com.devshawon.curehealthcare.models.ProductResponse
import com.devshawon.curehealthcare.models.RegistrationResponse
import com.devshawon.curehealthcare.models.SingleOrderProduct
import com.devshawon.curehealthcare.models.SingleOrderResponse
import com.devshawon.curehealthcare.models.UpdatePassword
import com.devshawon.curehealthcare.models.UpdatePasswordResponse
import com.devshawon.curehealthcare.models.UpdateProfileRequest
import com.devshawon.curehealthcare.models.UpdateProfileResponse
import com.devshawon.curehealthcare.network.Resource
import com.devshawon.curehealthcare.network.Status
import com.devshawon.curehealthcare.ui.repository.Repository
import com.devshawon.curehealthcare.useCase.result.Event
import com.devshawon.curehealthcare.util.PreferenceStorage
import com.devshawon.curehealthcare.util.getDouble
import com.devshawon.curehealthcare.util.getInt
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val repository: Repository,
    var preferences: PreferenceStorage,
) : ViewModel() {

    var firstName = MutableLiveData<String>()
    var lastName = MutableLiveData<String>()
    var shopName = MutableLiveData<String>()
    var license = MutableLiveData<String>()
    var nid = MutableLiveData<String>()
    var mobile = MutableLiveData<String>()
    var shopAddress = MutableLiveData<String>()
    var pageCount = MutableLiveData<Int>()
    var trendingPageCount = MutableLiveData<Int>()
    var totalPrice = MutableLiveData<Double>()

    var bannerRequest = MutableLiveData<Event<Unit>>()
    var bannerList = ArrayList<String>()
    var bannerListResponse : LiveData<Resource<BannerResponseMobile>> = bannerRequest.switchMap {
        repository.getBannerList()
    }

    var productRequest = MutableLiveData<Event<ProductRequest>>()
    var productList = ArrayList<ProductData>()
    var productListLiveData = MutableLiveData<ArrayList<ProductData>>()
    var productListResponse : LiveData<Resource<ProductResponse>> = productRequest.switchMap {
        repository.getProduct(it.peekContent())
    }

    var searchRequest = MutableLiveData<Event<ProductRequest>>()
    var searchList = ArrayList<ProductData>()
    var searchListResponse : LiveData<Resource<ProductResponse>> = searchRequest.switchMap {
        repository.getProduct(it.peekContent())
    }

    var trendingRequest = MutableLiveData<Event<Int>>()
    var trendingList = ArrayList<ProductData>()
    var trendingListResponse : LiveData<Resource<ProductResponse>> = trendingRequest.switchMap {
        repository.getTrending(it.peekContent())
    }


    var notificationPageCount = MutableLiveData<Int>()

    var notificationRequest = MutableLiveData<Event<Int>>()
    var notificationList = ArrayList<NotificationResponseData>()
    var notificationResponse : LiveData<Resource<NotificationResponse>> = notificationRequest.switchMap {
        repository.getNotifications(it.peekContent())
    }

    var oldPassword = MutableLiveData<String>()
    var newPassword = MutableLiveData<String>()
    var confirmNewPassword = MutableLiveData<String>()

    var updatePasswordRequest = MutableLiveData<UpdatePassword>()
    var updatePasswordEvent = MutableLiveData<Event<String>>()
    var updatePasswordResponse : LiveData<Resource<UpdatePasswordResponse>> = updatePasswordRequest.switchMap {
        repository.updatePassword(it)
    }

    val editProfileEvent = MutableLiveData<Event<Unit>>()
    val editProfileResponse : LiveData<Resource<EditProfileGetRequest>> = editProfileEvent.switchMap {
        repository.getEditProfile()
    }

    val updateProfileRequest = MutableLiveData<UpdateProfileRequest>()
    val updateProfileEvent = MutableLiveData<Event<String>>()
    val updateProfileResponse : LiveData<Resource<UpdateProfileResponse>> = updateProfileRequest.switchMap {
        repository.updateProfile(it)
    }
    var event = MutableLiveData<Event<String>>()
    var productEvent = MutableLiveData<Event<String>>()
    var productCount = MutableLiveData<Int>()
    var nextPage = MutableLiveData<String>()

    val buttonApiCall = MutableLiveData<Event<String>>()


    val companyRequest = MutableLiveData<Event<String>>()
    var companyList = ArrayList<Form>()
    val companyOrFormEvent = MutableLiveData<Event<String>>()
    val companyResponse : LiveData<Resource<CompanyResponse>> = companyRequest.switchMap {
        repository.getCompany(it.peekContent())
    }

    val placeOrderRequest = MutableLiveData<PlaceOrderRequest>()
    var message = MutableLiveData<String>()
    val placeEvent = MutableLiveData<Event<String>>()
    val placeOrderResponse : LiveData<Resource<PlaceOrderResponse>> = placeOrderRequest.switchMap {
        repository.postPlaceOrder(it)
    }

    val formRequest = MutableLiveData<Event<String>>()
    val formList = ArrayList<Form>()
    val formEvent = MutableLiveData<Event<String>>()
    val formResponse : LiveData<Resource<FormResponse>> = formRequest.switchMap {
        repository.getForm(it.peekContent())
    }

    val orderRequest = MutableLiveData<Event<Int>>()
    var orderPageCount = MutableLiveData(1)
    val orderList = ArrayList<OrderData>()
    val orderEvent = MutableLiveData<Event<String>>()
    val orderResponse : LiveData<Resource<OrderResponse>> = orderRequest.switchMap {
        repository.getOrder(it.peekContent())
    }

    val singleOrderRequest = MutableLiveData<Event<Int>>()
    val singleOrderList = ArrayList<SingleOrderProduct>()
    val singleOrderResponseData = MutableLiveData<SingleOrderResponse>()
    val singleOrderEvent = MutableLiveData<Event<String>>()
    val singleOrderResponse : LiveData<Resource<SingleOrderResponse>> = singleOrderRequest.switchMap {
        repository.getSingleOrder(it.peekContent())
    }

    val cancelOrderRequest = MutableLiveData<Event<CancelOrderRequest>>()
    val cancelOrderResponse : LiveData<Resource<RegistrationResponse>> = cancelOrderRequest.switchMap {
        repository.cancelOrder(it.peekContent())
    }

    val markAsReadRequest = MutableLiveData<EmptyRequest>()
    val markAsReadResponse : LiveData<Resource<RegistrationResponse>> = markAsReadRequest.switchMap {
        repository.markAsReadOrder(it)
    }

    var notificationCount = 0

    init {
        bannerListResponse.observeForever {
            if(it.status == Status.SUCCESS && it.data != null){
                bannerList = it.data.sliders as ArrayList<String>
                event.postValue(Event(it.status.name))
            }else if(it.status == Status.ERROR){
                event.postValue(Event(it.status.name))
            }
        }

        productListResponse.observeForever {
            if(it.status == Status.SUCCESS && it.data != null){
                pageCount.value = it.data.currentPage?:1
                it.data.data.forEach { d ->
                    val id  = d.id
                    preferences.productList?.forEach {pD->
                        if(id == pD?.id){
                            d.productCount = pD?.productCount
                            return@forEach
                        }
                    }

                }
                productList = it.data.data as ArrayList<ProductData>
                nextPage.value = it.data.nextPageUrl?:""
                productEvent.postValue(Event(it.status.name))
            }else if(it.status == Status.ERROR){
                productEvent.postValue(Event(it.status.name))
            }
        }

        trendingListResponse.observeForever {
            if(it.status == Status.SUCCESS && it.data != null){
                trendingList.clear()
                trendingPageCount.value = it.data.currentPage?:1
                it.data.data.forEach { d ->
                    val id  = d.id
                    preferences.productList?.forEach {pD->
                        if(id == pD?.id){
                            d.productCount = pD?.productCount
                            return@forEach
                        }
                    }

                }
                trendingList = it.data.data as ArrayList<ProductData>
                productEvent.postValue(Event(it.status.name))
            }else if(it.status == Status.ERROR){
                productEvent.postValue(Event(it.status.name))
            }
        }

        searchListResponse.observeForever {
            if(it.status == Status.SUCCESS && it.data != null){
                it.data.data.forEach { d ->
                    val id  = d.id
                    preferences.productList?.forEach {pD->
                        if(id == pD?.id){
                            d.productCount = pD?.productCount
                            return@forEach
                        }
                    }

                }
                searchList = it.data.data as ArrayList<ProductData>
                productEvent.postValue(Event(it.status.name))
            }else if(it.status == Status.ERROR){
                productEvent.postValue(Event(it.status.name))
            }
        }

        notificationResponse.observeForever {
            if(it.status == Status.SUCCESS && it.data != null){
                notificationPageCount.value = it.data.currentPage?:0
                notificationCount = 0
                it.data.data.forEach{d->
                    if(d.status == "Unread" ){
                        notificationCount++
                    }
                }
                notificationList = it.data.data as ArrayList<NotificationResponseData>
                productEvent.postValue(Event(it.status.name))
            }else if(it.status == Status.ERROR){
                productEvent.postValue(Event(it.status.name))
            }
        }

        updatePasswordResponse.observeForever {
            if(it.status == Status.SUCCESS && it.data != null){
                updatePasswordEvent.postValue(Event(it.data.message))
            }else if(it.status == Status.ERROR){
                updatePasswordEvent.postValue(Event(it.data?.message!!))
            }
        }

        editProfileResponse.observeForever {
            if(it.status == Status.SUCCESS && it.data != null){
                it.data.data?.let { d->
                    firstName.value = d.customer?.firstName?:""
                    lastName.value = d.customer?.lastName?:""
                    license.value = d.customer?.license?:""
                    nid.value = d.customer?.nid?:""
                    shopName.value = d.customer?.shopName?:""
                    shopAddress.value = d.customer?.shopAddress?:""
                    mobile.value = d.phone?:""
                }
            }else if(it.status == Status.ERROR){

            }
        }

        updateProfileResponse.observeForever {
            if(it.status == Status.SUCCESS && it.data != null){
                it.data.let { d->
                    firstName.value = d.customer?.firstName?:""
                    lastName.value = d.customer?.lastName?:""
                    license.value = d.customer?.license?:""
                    nid.value = d.customer?.nid?:""
                    shopName.value = d.customer?.shopName?:""
                    shopAddress.value = d.customer?.shopAddress?:""
                }
                updateProfileEvent.postValue(Event(it.data.message?:"h"))
            }else if(it.status == Status.ERROR){
                updateProfileEvent.postValue(Event(it.data?.message!!))
            }
        }

        companyResponse.observeForever {
            if(it.status == Status.SUCCESS && it.data != null){
                companyList.clear()
                it.data.let { d->
                    d.forms.forEach { id ->
                        if(preferences.companyList?.contains(id.id.toString()) == true){
                            val form = Form()
                            form.checkBox = true
                            form.id = id.id
                            form.name = id.name
                            companyList.add(0,form)
                        }else{
                            companyList.add(id)
                        }
                    }
                    companyOrFormEvent.postValue(Event(it.status.name))
                }
            }else if(it.status == Status.ERROR){
                companyOrFormEvent.postValue(Event(it.status.name))
            }
        }
        formResponse.observeForever {
            if(it.status == Status.SUCCESS && it.data != null){
                formList.clear()
                it.data.let { d->
                    d.forms.forEach { id ->
                        if(preferences.formList?.contains(id.id.toString()) == true){
                            val form = Form()
                            form.checkBox = true
                            form.id = id.id
                            form.name = id.name
                            formList.add(0,form)
                        }else{
                            formList.add(id)
                        }
                    }
                    formEvent.postValue(Event(it.status.name))
                }
            }else if(it.status == Status.ERROR){
                formEvent.postValue(Event(it.status.name))
            }
        }

        orderResponse.observeForever {
            if(it.status == Status.SUCCESS && it.data != null){
                orderPageCount.value = it.data.currentPage?:1
                orderList.clear()
                it.data.let { d->
                    d.data?.let { it1 -> orderList.addAll(it1) }
                    orderEvent.postValue(Event(it.data.currentPage!!.toString()))
                }
            }else if(it.status == Status.ERROR){
                orderEvent.postValue(Event(it.error?.message?:"Error"))
            }
        }

        placeOrderResponse.observeForever {
            if(it.status == Status.SUCCESS && it.data !=null){
                message.value = it.data.message
                placeEvent.postValue(Event(it.status.name?:""))
            }else if(it.status == Status.ERROR){
                placeEvent.postValue(Event("Can not created, Please contact with the helpline number"))
            }
        }

        singleOrderResponse.observeForever {
            if(it.status == Status.SUCCESS && it.data !=null){
                it.data.let {d ->
                    singleOrderList.clear()
                    totalPrice.value = 0.00
                    singleOrderResponseData.postValue(it.data!!)
                    d.products.forEach {list->
                        singleOrderList.add(list)
                        totalPrice.value = totalPrice.value!! + (getDouble(list.salePrice) * getDouble(list.quantity))
                    }
                }
                singleOrderEvent.postValue(Event(it.status.name))
            }else if(it.status == Status.ERROR){
                singleOrderEvent.postValue(Event(it.status.name))
            }
        }

        cancelOrderResponse.observeForever {
            if(it.status == Status.SUCCESS && it.data !=null){
                message.value = it.data.message ?:""
                placeEvent.postValue(Event(it.status.name))
            }else if(it.status == Status.ERROR){
                message.value = it.data?.message ?:""
            }
        }

        markAsReadResponse.observeForever {
            if(it.status == Status.SUCCESS && it.data !=null){
                message.value = it.data.message ?:""

            }else if(it.status == Status.ERROR){
                message.value = it.data?.message ?:""
            }
        }
    }

    fun resetData(){
        pageCount.value = 1
        orderPageCount.value = 1
        notificationPageCount.value = 1
        trendingPageCount.value = 1
        totalPrice.value = 0.00
    }
}