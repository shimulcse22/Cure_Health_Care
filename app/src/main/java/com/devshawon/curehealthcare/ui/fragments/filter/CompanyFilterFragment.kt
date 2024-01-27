package com.devshawon.curehealthcare.ui.fragments.filter

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.devshawon.curehealthcare.R
import com.devshawon.curehealthcare.base.ui.BaseFragment
import com.devshawon.curehealthcare.dagger.viewModel.AppViewModelFactory
import com.devshawon.curehealthcare.databinding.CompanyFilterFragmentBinding
import com.devshawon.curehealthcare.models.Form
import com.devshawon.curehealthcare.network.Status
import com.devshawon.curehealthcare.ui.CureHealthCareActivity
import com.devshawon.curehealthcare.ui.fragments.HomeViewModel
import com.devshawon.curehealthcare.useCase.result.Event
import com.devshawon.curehealthcare.useCase.result.EventObserver
import kotlinx.coroutines.launch
import java.util.Collections
import javax.inject.Inject

class CompanyFilterFragment :
    BaseFragment<CompanyFilterFragmentBinding>(R.layout.company_filter_fragment) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val viewModel: HomeViewModel by navGraphViewModels(R.id.cure_health_care_nav_host_xml) { viewModelFactory }
    lateinit var adapter: SingleItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            viewModel.companyRequest.postValue(Event(""))
        }
        adapter = SingleItemAdapter(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.companyFilterRecyclerViewAll.adapter = adapter
        mBinding.companyFilterRecyclerViewAll.itemAnimator = DefaultItemAnimator()
        mBinding.companyFilterRecyclerViewAll.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        viewModel.companyOrFormEvent.observe(viewLifecycleOwner, EventObserver {
            if (it == Status.SUCCESS.name) {
                adapter.updateList(viewModel.companyList)
            }
        })

       SingleItemAdapter.execute = { form: Form, i: Int ,isSelected :Boolean->
           mBinding.companyFilterRecyclerViewAll.postDelayed({
               viewModel.companyList.removeAt(i)
               if(isSelected){
                   viewModel.companyList.add(0,form)
                   form.id?.let { (activity as CureHealthCareActivity).companyListLiveData.add(it.toString()) }
               }else{
                   viewModel.companyList.add(viewModel.companyList.size,form)
                   if((activity as CureHealthCareActivity).companyListLiveData.contains(form.id.toString())) (activity as CureHealthCareActivity).companyListLiveData.remove(form.id.toString())
               }
               adapter.updateList(viewModel.companyList)
           }, 100)
       }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.companyList.forEach {
            if(it.checkBox == true){
                preferences.companyList?.add(it.id.toString())
            }
        }
    }
}