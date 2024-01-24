package com.devshawon.curehealthcare.ui.fragments.filter

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import com.devshawon.curehealthcare.R
import com.devshawon.curehealthcare.base.ui.BaseFragment
import com.devshawon.curehealthcare.dagger.viewModel.AppViewModelFactory
import com.devshawon.curehealthcare.databinding.FilterFragmentBinding
import com.devshawon.curehealthcare.models.Form
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject

class FilterFragment : BaseFragment<FilterFragmentBinding>(R.layout.filter_fragment) {

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
            execute.invoke("apply",mActivity.companyListLiveData as ArrayList<String>,mActivity.formListLiveData as ArrayList<String>)
            backToHome()
        }
        mBinding.resetBtn.setOnClickListener {
            execute.invoke("reset", arrayListOf(), arrayListOf())
            backToHome()
        }
    }

    companion object{
        var execute : (data: String, cList : ArrayList<String>, fList :ArrayList<String>) -> Unit = { data: String, sList: ArrayList<String>, fList :ArrayList<String>-> }
    }
}