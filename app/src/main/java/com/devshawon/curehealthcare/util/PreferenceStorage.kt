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
    var customerName : String
    var shopAddress : String
    var isLogin : Boolean
    var productList: MutableList<ProductData?>?
}

class SharedPreferenceStorage(context: Context) : PreferenceStorage {
    private val prefs: Lazy<SharedPreferences> = lazy {
        context.sharedPreferences(PREFS_NAME)
    }
    override var isLogin by BooleanPreference(prefs, IS_LOGGED_IN)
    override var token by StringPreference(prefs, PREF_TOKEN, "")
    override var refreshToken by StringPreference(prefs, PREF_REFRESH_TOKEN, "")
    override var mobileNumber by StringPreference(prefs, MOBILE_NUMBER, "")
    override  var customerName by StringPreference(prefs, CUSTOMER_NAME,"")
    override  var shopAddress by StringPreference(prefs, SHOP_ADDRESS,"")
    override var productList by ProductListPreference(
        prefs,
        PRODUCT_DATA,
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

class StringPreferenceFloat(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: Float
) : ReadWriteProperty<Any, Float?> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Float {
        return preferences.value.getFloat(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Float?) {
        preferences.value.edit { putFloat(name, value!!) }
    }
}

class IntPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: Int
) : ReadWriteProperty<Any, Int> {
    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Int {
        return preferences.value.getInt(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
        preferences.value.edit { putInt(name, value) }
    }
}

class LongPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: Long
) : ReadWriteProperty<Any, Long> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Long {
        return preferences.value.getLong(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Long) {
        preferences.value.edit { putLong(name, value) }
    }
}

class ExcludeLayoutList(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: String
) : ReadWriteProperty<Any, List<String>?> {
    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): List<String>? {
        val moshe = Moshi.Builder().build()
        val value = preferences.value.getString(name, defaultValue) ?: defaultValue
        val listOfLayout: Type =
            Types.newParameterizedType(List::class.java, String::class.java)
        val jsonAdapter: JsonAdapter<List<String>?> =
            moshe.adapter(listOfLayout)
        return try {
            var list = jsonAdapter.fromJson(value)
            if(list == null){
                list = listOf()
            }
            list
        } catch (e: Exception) {
            listOf()
        }
    }

    override fun setValue(
        thisRef: Any,
        property: KProperty<*>,
        value: List<String>?
    ) {
        val moshe = Moshi.Builder().build()
        val listOfLayout: Type =
            Types.newParameterizedType(List::class.java, String::class.java)
        val jsonAdapter: JsonAdapter<List<String>?> =
            moshe.adapter(listOfLayout)
        val json = jsonAdapter.toJson(value)
        preferences.value.edit { putString(name, json) }
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
            jsonAdapter.fromJson(value)?: mutableListOf()
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

