package com.devshawon.curehealthcare.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.devshawon.curehealthcare.CureHealthCareApplication
import com.devshawon.curehealthcare.R
import com.devshawon.curehealthcare.base.ui.BaseActivity
import com.devshawon.curehealthcare.dagger.viewModel.AppViewModelFactory
import com.devshawon.curehealthcare.databinding.ActivityCureHealthCareBinding
import com.devshawon.curehealthcare.models.ProductData
import com.devshawon.curehealthcare.ui.fragments.HomeViewModel
import com.devshawon.curehealthcare.ui.fragments.UpdateCart
import com.devshawon.curehealthcare.useCase.result.Event
import com.devshawon.curehealthcare.util.PreferenceStorage
import com.devshawon.curehealthcare.util.getAmount
import com.devshawon.curehealthcare.util.removeBadge
import com.devshawon.curehealthcare.util.showBadge
import javax.inject.Inject

class CureHealthCareActivity :
    BaseActivity<ActivityCureHealthCareBinding>(R.layout.activity_cure_health_care), UpdateCart {
    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    @Inject
    lateinit var preferences: PreferenceStorage
    private val viewModel by viewModels<HomeViewModel> { viewModelFactory }
    lateinit var cureHealthCareApp: CureHealthCareApplication
    val productListLiveData: MutableList<ProductData> = mutableListOf()
    var productListActivity: ArrayList<ProductData> = arrayListOf()
    val companyListLiveData: MutableList<String?> = mutableListOf()
    val formListLiveData: MutableList<String?> = mutableListOf()
    var productPrice: Double = 0.00
    var itemCount: Int = 0
    val live = MutableLiveData<Event<Unit>>()
    var notificationCount = 0

    var item: Int = -1
    private val mNavController by lazy { (supportFragmentManager.findFragmentById(R.id.cureHealthCareNavHostFragment) as NavHostFragment).navController }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.lifecycleOwner = this
        mBinding.viewModel = viewModel
        cureHealthCareApp = this.application as CureHealthCareApplication

        mBinding.priceLayout.visibility = View.GONE
        if (preferences.productList!!.isNotEmpty()) {
            productListLiveData.clear()
            preferences.productList!!.forEach {
                productListLiveData.add(it!!)
                productPrice += getAmount(it.salePrice) * it.productCount!!
            }
            showOrHideBadge()
            mBinding.priceLayout.visibility = View.VISIBLE
            itemCount = productListLiveData.size
            mBinding.totalItemCount = itemCount.toString()
            mBinding.totalPriceCount = productPrice.toString()
        }
        if (preferences.companyList!!.isNotEmpty()) {
            companyListLiveData.clear()
            preferences.companyList!!.forEach {
                companyListLiveData.add(it!!)
            }
        }
        if (preferences.formList!!.isNotEmpty()) {
            formListLiveData.clear()
            preferences.formList!!.forEach {
                formListLiveData.add(it!!)
            }
        }
        mBinding.bottomNavView.setupWithNavController(mNavController)

        mNavController.addOnDestinationChangedListener { _, destination, _ ->
            val selectId = destination.id

            if(BACK_STACK_NOT_ENTRY.contains(selectId)){
                mBinding.bottomNavView.visibility = View.GONE
            }else{
                mBinding.bottomNavView.visibility = View.VISIBLE
            }
            if (PRICE_LAYOUT_NOT_SHOWING.contains(selectId)) {
                mBinding.priceLayout.visibility = View.GONE
            } else {
                if (productListLiveData.isNotEmpty()) {
                    mBinding.priceLayout.visibility = View.VISIBLE
                } else {
                    mBinding.priceLayout.visibility = View.GONE
                }
            }
        }
    }

    override fun onStop() {
        preferences.productList = productListLiveData.toMutableList()
        //preferences.companyList = companyListLiveData.toMutableList()
        //preferences.formList = formListLiveData.toMutableList()
        super.onStop()
    }

    fun getStackCount(): Int {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.cureHealthCareNavHostFragment) as NavHostFragment?
        return navHostFragment!!.childFragmentManager.backStackEntryCount
    }

    companion object {
        private val TOP_LEVEL_DESTINATIONS = setOf(
            R.id.homeFragment,
            R.id.trendingFragment,
            R.id.orderFragment,
            R.id.cartFragment,
            R.id.profileFragment
        )
        private val PRICE_LAYOUT_NOT_SHOWING = setOf(
            R.id.cartFragment, R.id.filter_fragment
        )
        val BACK_STACK_NOT_ENTRY = setOf(
            R.id.singleOrderFragment, R.id.search_fragment, R.id.notification_fragment,R.id.chagePassword,R.id.editProfileFragment
        )
    }

    override fun inCreaseItem(data: ProductData) {
        productPrice += getAmount(data.salePrice)
        mBinding.totalPriceCount = productPrice.toString()
        var bool = false
        productListLiveData.forEach {
            if (it.id == data.id) {
                bool = true
                it.productCount = data.productCount
                live.postValue(Event(Unit))
                return@forEach
            }
        }

        if (!bool) {
            productListLiveData.add(data)
            itemCount = productListLiveData.size
            mBinding.totalItemCount = itemCount.toString()
        }
        if (productListLiveData.isNotEmpty()) mBinding.priceLayout.visibility = View.VISIBLE
    }

    override fun decreaseItem(data: ProductData, position: Int, isDelete: Boolean) {
        productPrice -= if (isDelete) {
            getAmount(data.salePrice) * position
        } else {
            getAmount(data.salePrice)
        }
        mBinding.totalPriceCount = productPrice.toString()
        var id = -1
        if (data.productCount == 0) {
            productListLiveData.forEachIndexed { index, productData ->
                if (productData.id == data.id) {
                    id = index
                    productData.productCount = data.productCount
                    live.postValue(Event(Unit))
                    return@forEachIndexed
                }
            }
            productListLiveData.removeAt(id)
            itemCount = productListLiveData.size
            mBinding.totalItemCount = itemCount.toString()
            if (productListLiveData.size == 0) mBinding.priceLayout.visibility = View.GONE

            showBadge(
                this, mBinding.bottomNavView, R.id.cartFragment, productListLiveData.size.toString()
            )
        } else {
            productListLiveData.forEach {
                if (it.id == data.id) {
                    it.productCount = data.productCount
                    live.postValue(Event(Unit))
                    return@forEach
                }
            }

        }
    }

    fun showOrHideBadge() {
        val badge: Int = productListLiveData.size
        if (badge > 0) {
            showBadge(this, mBinding.bottomNavView, R.id.cartFragment, badge.toString())
        } else {
            removeBadge(mBinding.bottomNavView, R.id.cartFragment)
        }
    }
}