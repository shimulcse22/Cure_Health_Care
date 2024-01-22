package com.devshawon.curehealthcare.ui.fragments.cart

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.devshawon.curehealthcare.R
import com.devshawon.curehealthcare.base.ui.BaseFragment
import com.devshawon.curehealthcare.dagger.viewModel.AppViewModelFactory
import com.devshawon.curehealthcare.databinding.FragmentCartBinding
import com.devshawon.curehealthcare.models.ProductData
import com.devshawon.curehealthcare.ui.CureHealthCareActivity
import com.devshawon.curehealthcare.ui.adapter.ProductCartAdapter
import com.devshawon.curehealthcare.ui.fragments.HomeViewModel
import com.devshawon.curehealthcare.ui.fragments.OnItemClick
import com.devshawon.curehealthcare.ui.fragments.UpdateCart
import com.devshawon.curehealthcare.useCase.result.Event
import com.devshawon.curehealthcare.useCase.result.EventObserver
import kotlinx.coroutines.launch
import javax.inject.Inject

class CartFragment : BaseFragment<FragmentCartBinding>(R.layout.fragment_cart),OnItemClick {
    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val homeViewModel: HomeViewModel by navGraphViewModels(R.id.cure_health_care_nav_host_xml) { viewModelFactory }

    private lateinit var productAdapter: ProductCartAdapter
    var productList = MutableLiveData<ArrayList<ProductData>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productAdapter = ProductCartAdapter(onItemClick = this)
        productList.value = arrayListOf()

        productList.value = (activity as CureHealthCareActivity).productListLiveData  as ArrayList<ProductData>
        lifecycleScope.launch {
            productAdapter.updateContext(requireContext())
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = homeViewModel

        mBinding.medicineLayout.adapter = productAdapter
        mBinding.medicineLayout.itemAnimator = DefaultItemAnimator()
        mBinding.medicineLayout.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        productList.observe(viewLifecycleOwner) {
            if(it.isNotEmpty()){
                mBinding.uniqueItemCount.text = it.size.toString()
                mBinding.totalAmountCount.text = (activity as CureHealthCareActivity).productPrice.toString()
                productAdapter.updateProductList(it, 1)
            }
        }

        (activity as CureHealthCareActivity).live.observe(viewLifecycleOwner,EventObserver{
            mBinding.uniqueItemCount.text = (activity as CureHealthCareActivity).itemCount.toString()
            mBinding.totalAmountCount.text = (activity as CureHealthCareActivity).productPrice.toString()
        })
    }

    override fun onPlusIconClick(item: ProductData) {
        homeViewModel.productCount.value = homeViewModel.productCount.value ?: (0 + 1)
        (activity as CureHealthCareActivity).productListActivity.forEach {
            if(it.id == item.id){
                it.productCount = item.productCount
                return@forEach
            }
        }

        (activity as UpdateCart).inCreaseItem(item)
        (activity as CureHealthCareActivity).showOrHideBadge(0)
        mBinding.uniqueItemCount.text = (activity as CureHealthCareActivity).itemCount.toString()
        mBinding.totalAmountCount.text = (activity as CureHealthCareActivity).productPrice.toString()
    }

    override fun onMinusIconClick(item: ProductData,position: Int) {
        homeViewModel.productCount.value = homeViewModel.productCount.value ?: (0 - 1)
        (activity as CureHealthCareActivity).productListActivity.forEachIndexed { index, productData ->
            if(productData.id == item.id){
                productData.productCount = item.productCount
                return@forEachIndexed
            }
        }

        (activity as UpdateCart).decreaseItem(item,position)
        (activity as CureHealthCareActivity).showOrHideBadge(0)
    }
}