package com.devshawon.curehealthcare.ui.fragments.cart

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.devshawon.curehealthcare.CureHealthCareApplication
import com.devshawon.curehealthcare.R
import com.devshawon.curehealthcare.base.ui.BaseFragment
import com.devshawon.curehealthcare.dagger.viewModel.AppViewModelFactory
import com.devshawon.curehealthcare.databinding.FragmentCartBinding
import com.devshawon.curehealthcare.models.ProductData
import com.devshawon.curehealthcare.ui.CureHealthCareActivity
import com.devshawon.curehealthcare.ui.adapter.ProductAdapter
import com.devshawon.curehealthcare.ui.adapter.ProductCartAdapter
import com.devshawon.curehealthcare.ui.fragments.HomeViewModel
import com.devshawon.curehealthcare.ui.fragments.OnItemClick
import com.devshawon.curehealthcare.useCase.result.EventObserver
import kotlinx.coroutines.launch
import javax.inject.Inject

class CartFragment : BaseFragment<FragmentCartBinding>(R.layout.fragment_cart),OnItemClick {
    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val homeViewModel: HomeViewModel by navGraphViewModels(R.id.cure_health_care_nav_host_xml) { viewModelFactory }

    private lateinit var productAdapter: ProductCartAdapter
    val productList = MutableLiveData<ArrayList<ProductData>>()

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
    }

    override fun onPlusIconClick(item: ProductData) {

    }

    override fun onMinusIconClick(item: ProductData) {

    }
}