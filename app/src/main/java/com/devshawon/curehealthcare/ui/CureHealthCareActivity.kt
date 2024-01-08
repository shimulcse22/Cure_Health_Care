package com.devshawon.curehealthcare.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
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
import com.devshawon.curehealthcare.useCase.result.EventObserver
import com.devshawon.curehealthcare.util.PreferenceStorage
import com.devshawon.curehealthcare.util.getAmount
import com.devshawon.curehealthcare.util.removeBadge
import com.devshawon.curehealthcare.util.showBadge
import javax.inject.Inject

class CureHealthCareActivity : BaseActivity<ActivityCureHealthCareBinding>(R.layout.activity_cure_health_care),UpdateCart {
    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    @Inject
    lateinit var preferences: PreferenceStorage
    private val viewModel by viewModels<HomeViewModel> { viewModelFactory }
    private lateinit var cureHealthCareApp : CureHealthCareApplication
    val productListLiveData : MutableList<ProductData> = mutableListOf()
    val productListActivity : ArrayList<ProductData> = arrayListOf()
    val productId : ArrayList<Int> = arrayListOf()
    var productPrice : Double = 0.00

    var item : Int = -1
    private val mNavController by lazy { (supportFragmentManager.findFragmentById(R.id.cureHealthCareNavHostFragment) as NavHostFragment).navController }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.lifecycleOwner = this
        mBinding.viewModel = viewModel
        window.statusBarColor = ContextCompat.getColor(this, R.color.update_submit)
        cureHealthCareApp = this.application as CureHealthCareApplication
        if(preferences.productList!!.isNotEmpty()){
            productListLiveData.clear()
            preferences.productList!!.forEach {
                productListLiveData.add(it!!)
            }
            showOrHideBadge(0)
        }
        mBinding.bottomNavView.setupWithNavController(mNavController)

        mNavController.addOnDestinationChangedListener { _, destination, _ ->
            val selectId = destination.id
            val isTopLevelDestination = TOP_LEVEL_DESTINATIONS.contains(selectId)
            mBinding.bottomNavView.visibility =
                if (isTopLevelDestination) View.VISIBLE else View.GONE
        }
    }

    override fun onStop() {
        if(productListLiveData.isNotEmpty()){
            preferences.productList = productListLiveData.toMutableList()
        }
        super.onStop()
    }

    fun getStackCount(): Int {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.cureHealthCareNavHostFragment) as NavHostFragment?
        return navHostFragment!!.childFragmentManager.backStackEntryCount
    }

    companion object{
        private val TOP_LEVEL_DESTINATIONS = setOf(
            R.id.homeFragment,
            R.id.trendingFragment,
            R.id.orderFragment,
            R.id.cartFragment,
            R.id.profileFragment
        )
    }

    override fun inCreaseItem(data: ProductData) {
        productPrice += getAmount(data.salePrice)
        var bool = false
        productListLiveData.forEach {
            if(it.id == data.id){
                bool = true
                it.productCount = data.productCount
                return@forEach
            }
        }

        if(!bool){
            productListLiveData.add(data)
        }
        productId.add(data.id?:0)
    }

    override fun decreaseItem(data: ProductData) {
        var id  =  -1
        if(data.productCount == 0){
            productListLiveData.forEachIndexed { index, productData ->
                if(productData.id == data.id){
                    id = index
                    return@forEachIndexed
                }
            }
            if(id != -1){
                productListLiveData.removeAt(id)
            }
            showBadge(this, mBinding.bottomNavView, R.id.cartFragment, productListLiveData.size.toString())
        }else{
            productListLiveData.forEach {
                if(it.id == data.id){
                    it.productCount = data.productCount
                    return@forEach
                }
            }
        }
    }

    fun showOrHideBadge(count: Int) {
        val badge : Int  = productListLiveData.size
        if (badge > 0) {
            showBadge(this, mBinding.bottomNavView, R.id.cartFragment, badge.toString())
        } else {
            removeBadge(mBinding.bottomNavView, R.id.cartFragment)
        }
    }
}