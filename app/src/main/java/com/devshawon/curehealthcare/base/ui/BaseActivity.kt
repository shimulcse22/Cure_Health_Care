package com.devshawon.curehealthcare.base.ui

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.ViewPumpAppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.devshawon.curehealthcare.base.navigation.ActivityScreenSwitcher
import dagger.android.support.DaggerAppCompatActivity
import dev.b3nedikt.app_locale.AppLocale
import javax.inject.Inject

abstract class BaseActivity<T : ViewDataBinding> constructor(@LayoutRes private val mContentLayoutId : Int):
    DaggerAppCompatActivity(), NavigationHost
{
    @Inject
    lateinit var mActivityScreenSwitcher: ActivityScreenSwitcher
    protected val mBinding: T by lazy(LazyThreadSafetyMode.NONE) {
        DataBindingUtil.setContentView<T>(this, mContentLayoutId)
    }
    var mActivity: Activity? = null


    private val appCompatDelegate: AppCompatDelegate by lazy {
        ViewPumpAppCompatDelegate(
            baseDelegate = super.getDelegate(),
            baseContext = this,
            wrapContext = { baseContext -> AppLocale.wrap(baseContext) }
        )
    }

    override fun getDelegate(): AppCompatDelegate {
        return appCompatDelegate
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adjustFontScale(resources.configuration)
        mActivity = this
        mBinding.lifecycleOwner = this
    }

    override fun onResume() {
        mActivityScreenSwitcher.attach(this)
        super.onResume()
    }

    override fun onPause() {
        mActivityScreenSwitcher.detach()

        super.onPause()
    }


    override fun registerToolbarWithNavigation(toolbar: Toolbar) {
    }

    protected fun onArrowClick() = mActivityScreenSwitcher.goBack()

    override fun activityScreenSwitcher() = mActivityScreenSwitcher
    private fun adjustFontScale(configuration: Configuration?) {

        configuration?.let {
            it.fontScale = 1.0F
            val metrics: DisplayMetrics = resources.displayMetrics
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                val display = display
                display?.getRealMetrics(metrics)
            } else {
                @Suppress("DEPRECATION")
                val display = windowManager.defaultDisplay
                @Suppress("DEPRECATION")
                display.getMetrics(metrics)
            }
            metrics.scaledDensity = configuration.fontScale * metrics.density

            baseContext.applicationContext.createConfigurationContext(it)
            baseContext.resources.displayMetrics.setTo(metrics)
        }
    }
}