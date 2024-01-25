package com.devshawon.curehealthcare.base.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import com.devshawon.curehealthcare.dagger.viewModel.AppViewModelFactory
import com.devshawon.curehealthcare.ui.CureHealthCareActivity
import com.devshawon.curehealthcare.util.PreferenceStorage
import com.devshawon.curehealthcare.util.autoCleared
import com.devshawon.curehealthcare.util.navigateUp
import dagger.android.support.DaggerFragment
import timber.log.Timber
import javax.inject.Inject

abstract class BaseFragment<T : ViewDataBinding> constructor(@LayoutRes private val mContentLayoutId: Int) :
    DaggerFragment() {

    private var navigationHost: NavigationHost? = null
    var mBinding by autoCleared<T>()
    private var mToolbar: Toolbar? = null

    @Inject
    lateinit var preferences: PreferenceStorage

    @Inject
    lateinit var viewModelFactoryBase: AppViewModelFactory

    lateinit var mActivity: FragmentActivity

    override fun onAttach(newBase: Context) {
        super.onAttach(newBase)
        navigationHost = newBase as NavigationHost
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.let {
            mActivity = it as CureHealthCareActivity
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(
            inflater, mContentLayoutId, container, false
        )
        mBinding.lifecycleOwner = viewLifecycleOwner
        val rootView = mBinding.root
        initToolbar(rootView)

        return rootView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mToolbar = null

    }

    override fun onDetach() {
        super.onDetach()
        navigationHost = null

        Timber.i("BaseFragment-> onDetach Called")
    }

    private fun initToolbar(view: View) {
        Timber.i("BaseFragment-> initToolbar Called")

        val toolbarResId = resToolbarId()
        if (haveToolbar() && toolbarResId != 0) {
            mToolbar = view.findViewById(resToolbarId()) ?: return
            mToolbar?.apply {
                navigationHost?.registerToolbarWithNavigation(this)
            }
        }
    }


    protected open fun resToolbarId(): Int = 0
    protected open fun haveToolbar(): Boolean = false
    protected fun getToolbar(): Toolbar? = mToolbar
    protected fun activityScreenSwitcher() = navigationHost?.activityScreenSwitcher()

    open fun backToHome() {
        Timber.e("backToHome")
        try {
            (requireContext() as CureHealthCareActivity).let {
                for (item in 0..it.getStackCount()) {
                    navigateUp()
                }
            }
        } catch (_: Exception) {

        }
    }

}