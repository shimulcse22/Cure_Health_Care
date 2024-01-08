package com.devshawon.curehealthcare.ui.fragments.filter

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
import com.devshawon.curehealthcare.databinding.FormFilterFragmentBinding
import com.devshawon.curehealthcare.network.Status
import com.devshawon.curehealthcare.ui.fragments.HomeViewModel
import com.devshawon.curehealthcare.useCase.result.Event
import com.devshawon.curehealthcare.useCase.result.EventObserver
import kotlinx.coroutines.launch
import javax.inject.Inject

class FormFilterFragment : BaseFragment<FormFilterFragmentBinding>(R.layout.form_filter_fragment) {
    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val viewModel: HomeViewModel by navGraphViewModels(R.id.cure_health_care_nav_host_xml) { viewModelFactory }
    lateinit var adapter: SingleItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            viewModel.formRequest.postValue(Event(""))
        }
        adapter = SingleItemAdapter(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.companyFilterRecyclerViewAll.adapter = adapter
        mBinding.companyFilterRecyclerViewAll.itemAnimator = DefaultItemAnimator()
        mBinding.companyFilterRecyclerViewAll.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewModel.formEvent.observe(viewLifecycleOwner, EventObserver {
            if (it == Status.SUCCESS.name) {
                adapter.updateList(viewModel.formList)
            }
        })
    }
}