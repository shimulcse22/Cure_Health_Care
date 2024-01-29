package com.devshawon.curehealthcare.ui.fragments.cart

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.devshawon.curehealthcare.R
import com.devshawon.curehealthcare.base.ui.BaseFragment
import com.devshawon.curehealthcare.dagger.viewModel.AppViewModelFactory
import com.devshawon.curehealthcare.databinding.FragmentCartBinding
import com.devshawon.curehealthcare.models.Cart
import com.devshawon.curehealthcare.models.PlaceOrderRequest
import com.devshawon.curehealthcare.models.Product
import com.devshawon.curehealthcare.models.ProductData
import com.devshawon.curehealthcare.network.Status
import com.devshawon.curehealthcare.ui.CureHealthCareActivity
import com.devshawon.curehealthcare.ui.adapter.ProductCartAdapter
import com.devshawon.curehealthcare.ui.fragments.HomeViewModel
import com.devshawon.curehealthcare.ui.fragments.OnItemClick
import com.devshawon.curehealthcare.ui.fragments.UpdateCart
import com.devshawon.curehealthcare.useCase.result.EventObserver
import com.devshawon.curehealthcare.util.positiveButton
import com.devshawon.curehealthcare.util.showDialog
import kotlinx.coroutines.launch
import javax.inject.Inject

class CartFragment : BaseFragment<FragmentCartBinding>(R.layout.fragment_cart), OnItemClick {
    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val homeViewModel: HomeViewModel by navGraphViewModels(R.id.cure_health_care_nav_host_xml) { viewModelFactory }

    private lateinit var productAdapter: ProductCartAdapter
    private var productList = MutableLiveData<ArrayList<ProductData>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productAdapter = ProductCartAdapter(onItemClick = this)
        productList.value = arrayListOf()

        productList.value =
            (activity as CureHealthCareActivity).productListLiveData as ArrayList<ProductData>
        lifecycleScope.launch {
            productAdapter.updateContext(requireContext())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = homeViewModel

        mBinding.medicineLayout.adapter = productAdapter
        mBinding.medicineLayout.itemAnimator = DefaultItemAnimator()
        mBinding.medicineLayout.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        visibility()

        productList.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                mBinding.uniqueItemCount.text = it.size.toString()
                mBinding.totalAmountCount.text =
                    (activity as CureHealthCareActivity).productPrice.toString()
                productAdapter.updateProductList(it, 1)
            }
        }

        (activity as CureHealthCareActivity).live.observe(viewLifecycleOwner, EventObserver {
            mBinding.uniqueItemCount.text =
                (activity as CureHealthCareActivity).itemCount.toString()
            mBinding.totalAmountCount.text =
                (activity as CureHealthCareActivity).productPrice.toString()
            visibility()
        })

        homeViewModel.placeEvent.observe(viewLifecycleOwner,EventObserver{
            if(it == Status.SUCCESS.name){
                val data = (activity as CureHealthCareActivity).productListLiveData
                (activity as CureHealthCareActivity).productListActivity.forEach {d->
                    data.forEach {pd->
                        if(d.id == pd.id){
                            d.productCount = 0
                        }
                    }
                }
                data.clear()
                visibility()
                (activity as CureHealthCareActivity).showOrHideBadge()
                preferences.productList = mutableListOf()
                showDialog {
                    setTitle(getString(R.string.success))
                    setMessage(homeViewModel.message.value)
                    setIcon(R.drawable.right_sign)
                    positiveButton(getString(R.string.ok))
                }
            }else{
                showDialog {
                    setTitle(getString(R.string.error_title))
                    setMessage(it)
                    setIcon(R.drawable.ic_error)
                    positiveButton(getString(R.string.ok))
                }
            }
        })

        mBinding.placeOrder.setOnClickListener {
            val list = ArrayList<Product>()
            var total = 0
            (activity as CureHealthCareActivity).productListLiveData.forEach {
                total += it.productCount ?: 0
                val product = Product()
                product.id = it.id
                product.mrp = it.mrp
                product.discount = it.discount
                product.quantity = it.productCount
                product.salePrice = it.salePrice
                product.status = it.status
                list.add(product)
            }
            homeViewModel.placeOrderRequest.postValue(
                PlaceOrderRequest(
                    Cart(products = list, total = total)
                )
            )
        }
    }

    override fun onPlusIconClick(item: ProductData) {
        homeViewModel.productCount.value = homeViewModel.productCount.value ?: (0 + 1)
        (activity as CureHealthCareActivity).productListActivity.forEach {
            if (it.id == item.id) {
                it.productCount = item.productCount
                return@forEach
            }
        }

        (activity as UpdateCart).inCreaseItem(item)
        (activity as CureHealthCareActivity).showOrHideBadge()
        mBinding.uniqueItemCount.text = (activity as CureHealthCareActivity).itemCount.toString()
        mBinding.totalAmountCount.text =
            (activity as CureHealthCareActivity).productPrice.toString()

        val callBack : OnBackPressedCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                backToHome()
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,callBack)
    }

    override fun onMinusIconClick(item: ProductData, position: Int,isDelete : Boolean) {
        homeViewModel.productCount.value = homeViewModel.productCount.value ?: (0 - 1)
        (activity as CureHealthCareActivity).productListActivity.forEachIndexed { _, productData ->
            if (productData.id == item.id) {
                productData.productCount = item.productCount
                return@forEachIndexed
            }
        }

        (activity as UpdateCart).decreaseItem(item, position,isDelete)
        (activity as CureHealthCareActivity).showOrHideBadge()
    }

    private fun visibility(){
        if((activity as CureHealthCareActivity).productListLiveData.isNotEmpty()){
            mBinding.medicineLayout.visibility = View.VISIBLE
            mBinding.amount.visibility = View.VISIBLE
            mBinding.empty.visibility = View.GONE
        }else{
            mBinding.medicineLayout.visibility = View.GONE
            mBinding.amount.visibility = View.GONE
            mBinding.empty.visibility = View.VISIBLE
        }
    }
}