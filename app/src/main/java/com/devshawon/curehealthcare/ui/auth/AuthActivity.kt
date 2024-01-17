package com.devshawon.curehealthcare.ui.auth

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.devshawon.curehealthcare.R
import com.devshawon.curehealthcare.base.ui.BaseActivity
import com.devshawon.curehealthcare.databinding.AuthActivityLayoutBinding
import com.devshawon.curehealthcare.databinding.LoginFragmentLayoutBinding
import com.devshawon.curehealthcare.ui.DispatchInsetsNavHostFragment
import com.devshawon.curehealthcare.ui.auth.AuthActivityScreen.Companion.NAVIGATION_ID
import com.devshawon.curehealthcare.util.PreferenceStorage
import dev.b3nedikt.app_locale.AppLocale
import kotlinx.android.synthetic.main.auth_activity_layout.nav_host_fragment
import javax.inject.Inject

class AuthActivity : BaseActivity<AuthActivityLayoutBinding>(R.layout.auth_activity_layout) {

    companion object {
        private val TOP_LEVEL_DESTINATIONS = setOf(R.id.splash_fragment)
    }


    private val mNavController by lazy { (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController }
    var mContext: Activity? = null

    @Inject
    lateinit var preferences: PreferenceStorage
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.update_submit)
        val id = intent.getIntExtra(NAVIGATION_ID, R.id.splash_fragment)
        val navHostFragment = nav_host_fragment as NavHostFragment
        val graphInflater = navHostFragment.navController.navInflater
        val navGraph = graphInflater.inflate(R.navigation.auth_nav_graph)
        navGraph.setStartDestination(id)

        mNavController.graph = navGraph
        mContext = this

    }


    override fun registerToolbarWithNavigation(toolbar: Toolbar) {
        val appBarConfiguration = AppBarConfiguration(TOP_LEVEL_DESTINATIONS)
        toolbar.setupWithNavController(mNavController, appBarConfiguration)
    }
}
