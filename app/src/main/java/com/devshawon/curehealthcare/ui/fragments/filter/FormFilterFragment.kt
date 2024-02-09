package com.devshawon.curehealthcare.ui.fragments.filter

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.devshawon.curehealthcare.R
import com.devshawon.curehealthcare.base.ui.BaseFragment
import com.devshawon.curehealthcare.dagger.viewModel.AppViewModelFactory
import com.devshawon.curehealthcare.databinding.FormFilterFragmentBinding
import com.devshawon.curehealthcare.models.Form
import com.devshawon.curehealthcare.network.Status
import com.devshawon.curehealthcare.ui.CureHealthCareActivity
import com.devshawon.curehealthcare.ui.fragments.HomeViewModel
import com.devshawon.curehealthcare.useCase.result.Event
import com.devshawon.curehealthcare.useCase.result.EventObserver
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class FormFilterFragment : BaseFragment<FormFilterFragmentBinding>(R.layout.form_filter_fragment) {
    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val viewModel: HomeViewModel by navGraphViewModels(R.id.cure_health_care_nav_host_xml) { viewModelFactory }
    lateinit var adapter: SingleItemAdapterForm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            viewModel.formRequest.postValue(Event(""))
        }
        adapter = SingleItemAdapterForm(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.companyFilterRecyclerViewAll.adapter = adapter
        mBinding.companyFilterRecyclerViewAll.itemAnimator = DefaultItemAnimator()
        mBinding.companyFilterRecyclerViewAll.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        viewModel.formEvent.observe(viewLifecycleOwner, EventObserver {
            preferences.formList?.toMutableList()?.let { it1 -> (activity as CureHealthCareActivity).formListLiveData.addAll(it1) }
            if (it == Status.SUCCESS.name) {
                adapter.updateList(viewModel.formList)
            }
        })

        SingleItemAdapterForm.execute = { form: Form, i: Int, isSelected :Boolean->
            mBinding.companyFilterRecyclerViewAll.postDelayed({
                viewModel.formList.removeAt(i)
                if(isSelected){
                    viewModel.formList.add(0,form)
                    //form.id?.let { (activity as CureHealthCareActivity).formListLiveData.add(it.toString()) }
                }else{
                    viewModel.formList.add(viewModel.formList.size,form)
                    //if((activity as CureHealthCareActivity).formListLiveData.contains(id.toString())) (activity as CureHealthCareActivity).formListLiveData.remove(form.id.toString())
                }
                adapter.updateList(viewModel.formList)
            }, 100)
        }
    }

    override fun onDestroyView() {
        (activity as CureHealthCareActivity).formListLiveData.clear()
        viewModel.formList.forEach {
            if(it.checkBox == true){
                (activity as CureHealthCareActivity).formListLiveData.add(it.id.toString())
            }
        }
        super.onDestroyView()
    }

    internal class DebouncingQueryTextListener(
        lifecycle: Lifecycle,
        private val onDebouncingQueryTextChange: (String?) -> Unit
    ) : SearchView.OnQueryTextListener {
        private var debouncePeriod: Long = 500
        private val coroutineScope = lifecycle.coroutineScope
        private var searchJob: Job? = null

        override fun onQueryTextSubmit(query: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            searchJob?.cancel()
            searchJob = coroutineScope.launch {
                newText?.let {
                    delay(debouncePeriod)
                    onDebouncingQueryTextChange(newText)
                }
            }
            return false
        }
    }
}