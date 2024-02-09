package com.devshawon.curehealthcare.ui.fragments.filter

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.os.postDelayed
import androidx.fragment.app.setFragmentResult
import com.devshawon.curehealthcare.R
import com.devshawon.curehealthcare.base.ui.BaseFragment
import com.devshawon.curehealthcare.dagger.viewModel.AppViewModelFactory
import com.devshawon.curehealthcare.databinding.FilterFragmentBinding
import com.devshawon.curehealthcare.models.Form
import com.devshawon.curehealthcare.ui.CureHealthCareActivity
import com.devshawon.curehealthcare.ui.fragments.home.HomeFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject

class FilterFragment : BaseFragment<FilterFragmentBinding>(R.layout.filter_fragment) {

    override fun haveToolbar(): Boolean = true

    override fun resToolbarId(): Int = R.id.toolbar

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.viewPager.adapter = ViewPagerFilterFragment(this)

        val titleTab: Array<String> = arrayOf(
            getString(R.string.company_filter),
            getString(R.string.form_filter),
        )
        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)

        TabLayoutMediator(tabLayout, mBinding.viewPager) { tab, position ->
            tab.text = titleTab[position]
        }.attach()

        mBinding.applyBtn.setOnClickListener {
            Log.d("THE PROCESS IS 3","${(activity as CureHealthCareActivity).companyListLiveData}")
            Handler(Looper.getMainLooper()).postDelayed({
                execute.invoke("apply",(activity as CureHealthCareActivity).companyListLiveData,(activity as CureHealthCareActivity).formListLiveData )
                backToHome()
            },1000)

        }
        mBinding.resetBtn.setOnClickListener {
            Handler(Looper.getMainLooper()).postDelayed({
                execute.invoke("reset", arrayListOf(), arrayListOf())
                backToHome()
            },1000)

        }

        mBinding.toolbar.setNavigationOnClickListener {
            backToHome()
        }
    }


    companion object{
        var execute : (data: String, cList : MutableList<String?>, fList :MutableList<String?>) -> Unit = { data: String, sList: MutableList<String?>, fList :MutableList<String?>-> }
    }
}