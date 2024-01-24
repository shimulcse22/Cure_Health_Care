package com.devshawon.curehealthcare.util

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.WorkerThread
import androidx.core.content.edit
import com.devshawon.curehealthcare.models.ProductData
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.Type
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

interface PreferenceStorage {
    var refreshToken: String
    var token: String
    var mobileNumber: String
    var customerName: String
    var shopAddress: String
    var isLogin: Boolean
    var productList: MutableList<ProductData?>?
    var companyList: MutableList<Int?>?
    var formList: MutableList<Int?>?
}

class SharedPreferenceStorage(context: Context) : PreferenceStorage {
    private val prefs: Lazy<SharedPreferences> = lazy {
        context.sharedPreferences(PREFS_NAME)
    }
    override var isLogin by BooleanPreference(prefs, IS_LOGGED_IN)
    override var token by StringPreference(prefs, PREF_TOKEN, "")
    override var refreshToken by StringPreference(prefs, PREF_REFRESH_TOKEN, "")
    override var mobileNumber by StringPreference(prefs, MOBILE_NUMBER, "")
    override var customerName by StringPreference(prefs, CUSTOMER_NAME, "")
    override var shopAddress by StringPreference(prefs, SHOP_ADDRESS, "")
    override var productList by ProductListPreference(
        prefs,
        PRODUCT_DATA,
        ""
    )
    override var companyList by CompanyOrFormListPreference(
        prefs,
        COMPANY_DATA,
        ""
    )
    override var formList by CompanyOrFormListPreference(
        prefs,
        FORM_DATA,
        ""
    )

    companion object {
        const val PREFS_NAME = "cure_health_care"
        const val PREF_TOKEN = "pref_token"
        const val PREF_REFRESH_TOKEN = "pref_refreshToken"
        const val MOBILE_NUMBER = "mobile_number"
        const val CUSTOMER_NAME = "customer_name"
        const val SHOP_ADDRESS = "shop_address"
        const val PRODUCT_DATA = "product_data"
        const val COMPANY_DATA = "company_data"
        const val FORM_DATA = "form_data"
        const val IS_LOGGED_IN = "logged_in"
    }
}

class BooleanPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: Boolean = false
) : ReadWriteProperty<Any, Boolean> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
        return preferences.value.getBoolean(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
        preferences.value.edit { putBoolean(name, value) }
    }
}


class StringPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: String
) : ReadWriteProperty<Any, String?> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): String {
        return preferences.value.getString(name, defaultValue) ?: defaultValue
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) {
        preferences.value.edit { putString(name, value) }
    }
}

class ProductListPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: String
) : ReadWriteProperty<Any, MutableList<ProductData?>?> {
    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): MutableList<ProductData?>? {
        val moshi = Moshi.Builder().build()
        val value = preferences.value.getString(name, defaultValue) ?: defaultValue
        val listOfCardsType: Type =
            Types.newParameterizedType(MutableList::class.java, ProductData::class.java)
        val jsonAdapter: JsonAdapter<MutableList<ProductData?>?> = moshi.adapter(listOfCardsType)
        return try {
            jsonAdapter.fromJson(value) ?: mutableListOf()
        } catch (e: Exception) {
            mutableListOf()
        }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: MutableList<ProductData?>?) {
        val moshi = Moshi.Builder().build()
        val listOfCardsType: Type =
            Types.newParameterizedType(MutableList::class.java, ProductData::class.java)
        val jsonAdapter: JsonAdapter<MutableList<ProductData?>?> =
            moshi.adapter(listOfCardsType)
        val json = jsonAdapter.toJson(value)
        preferences.value.edit { putString(name, json) }
    }
}

class CompanyOrFormListPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: String
) : ReadWriteProperty<Any, MutableList<Int?>?> {
    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): MutableList<Int?>? {
        val moshi = Moshi.Builder().build()
        val value = preferences.value.getString(name, defaultValue) ?: defaultValue
        val listOfCardsType: Type =
            Types.newParameterizedType(MutableList::class.java, Int::class.java)
        val jsonAdapter: JsonAdapter<MutableList<Int?>?> = moshi.adapter(listOfCardsType)
        return try {
            jsonAdapter.fromJson(value) ?: mutableListOf()
        } catch (e: Exception) {
            mutableListOf()
        }
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: MutableList<Int?>?) {
        val moshi = Moshi.Builder().build()
        val listOfCardsType: Type =
            Types.newParameterizedType(MutableList::class.java, Int::class.java)
        val jsonAdapter: JsonAdapter<MutableList<Int?>?> =
            moshi.adapter(listOfCardsType)
        val json = jsonAdapter.toJson(value)
        preferences.value.edit { putString(name, json) }
    }
}

