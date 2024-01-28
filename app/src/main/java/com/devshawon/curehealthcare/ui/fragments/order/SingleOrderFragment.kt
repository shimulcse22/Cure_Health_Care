package com.devshawon.curehealthcare.ui.fragments.order

import android.os.Bundle
import android.view.View
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import com.devshawon.curehealthcare.R
import com.devshawon.curehealthcare.base.ui.BaseFragment
import com.devshawon.curehealthcare.dagger.viewModel.AppViewModelFactory
import com.devshawon.curehealthcare.databinding.FragmentOrderDetailsLayoutBinding
import com.devshawon.curehealthcare.ui.fragments.HomeViewModel
import com.devshawon.curehealthcare.util.WrapContentLinearLayoutManager
import javax.inject.Inject

class SingleOrderFragment : BaseFragment<FragmentOrderDetailsLayoutBinding>(R.layout.fragment_order_details_layout) {
    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val viewModel: HomeViewModel by navGraphViewModels(R.id.cure_health_care_nav_host_xml) { viewModelFactory }

    private lateinit var singleOrderAdapter: SingleOrderAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        singleOrderAdapter = SingleOrderAdapter(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = viewModel

        mBinding.orderRecyclerView.adapter = singleOrderAdapter
        mBinding.orderRecyclerView.layoutManager = WrapContentLinearLayoutManager(requireContext())
        mBinding.orderRecyclerView.itemAnimator = DefaultItemAnimator()

        mBinding.orderIdTitle.text = "Order ID : "+viewModel.singleOrderResponseData.value?.id
        mBinding.orderTime.text = "Ordered at :"+viewModel.singleOrderResponseData.value?.orderPlacedAt
        mBinding.orderStatus.text = viewModel.singleOrderResponseData.value?.status

        if(viewModel.singleOrderResponseData.value?.status == "Pending"){
            mBinding.estimatedTitle.text = viewModel.singleOrderResponseData.value?.createdAt
        }else{
            mBinding.estimated.visibility = View.GONE
            mBinding.estimatedTitle.visibility = View.GONE
        }

        singleOrderAdapter.addList(viewModel.singleOrderList)
    }
}