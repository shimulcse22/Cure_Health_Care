package com.devshawon.curehealthcare.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.devshawon.curehealthcare.CureHealthCareApplication
import com.devshawon.curehealthcare.R
import com.devshawon.curehealthcare.base.ui.BaseActivity
import com.devshawon.curehealthcare.dagger.viewModel.AppViewModelFactory
import com.devshawon.curehealthcare.databinding.ActivityCureHealthCareBinding
import com.devshawon.curehealthcare.ui.fragments.HomeViewModel
import javax.inject.Inject

class CureHealthCareActivity : BaseActivity<ActivityCureHealthCareBinding>(R.layout.activity_cure_health_care) {
    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val viewModel by viewModels<HomeViewModel> { viewModelFactory }
    private lateinit var cureHealthCareApp: CureHealthCareApplication
    private val mNavController by lazy { (supportFragmentManager.findFragmentById(R.id.cureHealthCareNavHostFragment) as NavHostFragment).navController }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.lifecycleOwner = this
        mBinding.viewModel = viewModel
        cureHealthCareApp = this.application as CureHealthCareApplication
        mBinding.bottomNavView.setupWithNavController(mNavController)

        mNavController.addOnDestinationChangedListener { _, destination, _ ->
            val selectId = destination.id
            val isTopLevelDestination = TOP_LEVEL_DESTINATIONS.contains(selectId)
            mBinding.bottomNavView.visibility =
                if (isTopLevelDestination) View.VISIBLE else View.GONE
        }
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
}