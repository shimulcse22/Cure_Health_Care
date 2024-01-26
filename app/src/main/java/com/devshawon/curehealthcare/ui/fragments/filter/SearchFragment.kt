package com.devshawon.curehealthcare.ui.fragments.filter

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.devshawon.curehealthcare.R
import com.devshawon.curehealthcare.base.ui.BaseFragment
import com.devshawon.curehealthcare.dagger.viewModel.AppViewModelFactory
import com.devshawon.curehealthcare.databinding.SearchFragmentBinding
import com.devshawon.curehealthcare.models.ProductData
import com.devshawon.curehealthcare.models.ProductRequest
import com.devshawon.curehealthcare.ui.CureHealthCareActivity
import com.devshawon.curehealthcare.ui.adapter.ProductAdapter
import com.devshawon.curehealthcare.ui.fragments.HomeViewModel
import com.devshawon.curehealthcare.ui.fragments.OnItemClick
import com.devshawon.curehealthcare.ui.fragments.UpdateCart
import com.devshawon.curehealthcare.useCase.result.Event
import com.devshawon.curehealthcare.useCase.result.EventObserver
import com.devshawon.curehealthcare.util.WrapContentLinearLayoutManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


class SearchFragment : BaseFragment<SearchFragmentBinding>(R.layout.search_fragment),OnItemClick {
    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val viewModel: HomeViewModel by navGraphViewModels(R.id.cure_health_care_nav_host_xml) { viewModelFactory }

    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productAdapter = ProductAdapter(onItemClick = this)
        lifecycleScope.launch {
            productAdapter.updateContext(requireContext())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.medicineCompany.adapter = productAdapter
        mBinding.medicineCompany.itemAnimator = DefaultItemAnimator()
        mBinding.medicineCompany.layoutManager = WrapContentLinearLayoutManager(requireContext())

        viewModel.productEvent.observe(viewLifecycleOwner, EventObserver {
            (activity as CureHealthCareActivity).productListActivity.addAll(viewModel.searchList)
            if (viewModel.searchList.isNotEmpty()) {
                mBinding.medicineCompany.visibility = View.VISIBLE
                mBinding.noTitleText.visibility = View.GONE
                productAdapter.updateProductList(viewModel.searchList, 1)
            }else{
                mBinding.medicineCompany.visibility = View.GONE
                mBinding.noTitleText.visibility = View.VISIBLE
            }
        })


        mBinding.searchView.setOnQueryTextListener(
            DebouncingQueryTextListener(
                requireActivity().lifecycle
            ) { newText ->
                newText?.let {
                    if (it.isNotEmpty()) {
                        Log.d("SEARCH LIST 3","${viewModel.searchList.size}")
                        viewModel.searchRequest.postValue(Event(
                            ProductRequest(
                                search = it
                            )
                        ))
                    }
                }
            }
        )
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

    override fun onPlusIconClick(item: ProductData) {
        viewModel.productCount.value = viewModel.productCount.value ?: (0 + 1)
        (activity as UpdateCart).inCreaseItem(item)
        (activity as CureHealthCareActivity).showOrHideBadge(0)
    }

    override fun onMinusIconClick(item: ProductData, position: Int) {
        viewModel.productCount.value = viewModel.productCount.value ?: (0 - 1)
        (activity as UpdateCart).decreaseItem(item,position)
        (activity as CureHealthCareActivity).showOrHideBadge(0)
    }

}