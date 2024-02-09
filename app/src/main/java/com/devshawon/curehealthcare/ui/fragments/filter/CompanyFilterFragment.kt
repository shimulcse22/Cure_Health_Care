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
import androidx.recyclerview.widget.RecyclerView
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class CompanyFilterFragment :
    BaseFragment<CompanyFilterFragmentBinding>(R.layout.company_filter_fragment) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val viewModel: HomeViewModel by navGraphViewModels(R.id.cure_health_care_nav_host_xml) { viewModelFactory }
    lateinit var adapter: SingleItemAdapter

    private var companyList = ArrayList<Form>()

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
        mBinding.companyFilterRecyclerViewAll.layoutManager = WrapContentLinearLayoutManager()

        viewModel.companyOrFormEvent.observe(viewLifecycleOwner, EventObserver {
            preferences.companyList?.toMutableList()
                ?.let { list -> (activity as CureHealthCareActivity).companyListLiveData.addAll(list) }
            if (it == Status.SUCCESS.name) {
                companyList = viewModel.companyList
                adapter.updateList(companyList)
            }
        })

        mBinding.searchView.setOnQueryTextListener(
            DebouncingQueryTextListener(
                requireActivity().lifecycle
            ) { newText ->
                newText?.let {
                    if (it.isNotEmpty()) {
                        val filter = viewModel.companyList.filter {d->
                            d.name?.startsWith(it,ignoreCase = true)!! || d.checkBox == true
                        }
                        companyList = filter as ArrayList<Form>
                        adapter.updateList(companyList)
                        mBinding.companyFilterRecyclerViewAll.itemAnimator = null
                    }else{
                        companyList = viewModel.companyList
                        adapter.updateList(viewModel.companyList)
                        mBinding.companyFilterRecyclerViewAll.itemAnimator = DefaultItemAnimator()
                    }
                }
            }
        )

       SingleItemAdapter.execute = { form: Form, i: Int ,isSelected :Boolean->
           mBinding.companyFilterRecyclerViewAll.postDelayed({
               var id = 0
               var find = false
               viewModel.companyList.forEachIndexed { index, data ->
                   if(data.id == form.id){
                       id = index
                       find = true
                   }
               }
               if(find) viewModel.companyList.removeAt(id)
               if(isSelected){
                   viewModel.companyList.add(0,form)
               }else{
                   viewModel.companyList.add(viewModel.companyList.size,form)
               }
               companyList = viewModel.companyList
               adapter.updateList(companyList)
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

    inner class WrapContentLinearLayoutManager :
        LinearLayoutManager(requireContext(), VERTICAL, false) {
        override fun onLayoutChildren(
            recycler: RecyclerView.Recycler?, state: RecyclerView.State?
        ) {
            try {
                super.onLayoutChildren(recycler, state)
            } catch (_: IndexOutOfBoundsException) {

            }
        }
    }
}