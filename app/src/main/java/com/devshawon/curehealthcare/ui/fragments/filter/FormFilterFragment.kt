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

    private var formList = ArrayList<Form>()

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
                formList = viewModel.formList
                adapter.updateList(formList)
            }
        })

        mBinding.searchView.setOnQueryTextListener(DebouncingQueryTextListener(
                requireActivity().lifecycle
            ) { newText ->
                newText?.let {
                    if (it.isNotEmpty()) {
                        val filter = viewModel.formList.filter { d ->
                            d.name?.startsWith(it, ignoreCase = true)!! || d.checkBox == true
                        }
                        formList = filter as ArrayList<Form>
                        adapter.updateList(formList)
                        mBinding.companyFilterRecyclerViewAll.itemAnimator = null
                    } else {
                        formList = viewModel.formList
                        adapter.updateList(viewModel.formList)
                        mBinding.companyFilterRecyclerViewAll.itemAnimator = DefaultItemAnimator()
                    }
                }
            }
        )

        SingleItemAdapterForm.execute = { form: Form, _: Int, isSelected :Boolean->
            mBinding.companyFilterRecyclerViewAll.postDelayed({var id = 0
                var find = false
                viewModel.formList.forEachIndexed { index, data ->
                    if(data.id == form.id){
                        id = index
                        find = true
                    }
                }
                if(find) viewModel.formList.removeAt(id)
                if(isSelected){
                    viewModel.formList.add(0,form)
                }else{
                    viewModel.formList.add(viewModel.formList.size,form)
                }
                formList = viewModel.formList
                adapter.updateList(formList)
            }, 100)
        }
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