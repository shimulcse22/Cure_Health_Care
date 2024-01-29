package com.devshawon.curehealthcare.ui.fragments.order

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.FtsOptions.Order
import com.devshawon.curehealthcare.R
import com.devshawon.curehealthcare.base.ui.BaseFragment
import com.devshawon.curehealthcare.dagger.viewModel.AppViewModelFactory
import com.devshawon.curehealthcare.databinding.FragmentOrderBinding
import com.devshawon.curehealthcare.models.ProductRequest
import com.devshawon.curehealthcare.network.Status
import com.devshawon.curehealthcare.ui.OrderAdapter
import com.devshawon.curehealthcare.ui.fragments.HomeViewModel
import com.devshawon.curehealthcare.useCase.result.Event
import com.devshawon.curehealthcare.useCase.result.EventObserver
import com.devshawon.curehealthcare.util.getInt
import com.devshawon.curehealthcare.util.navigate
import kotlinx.coroutines.launch
import javax.inject.Inject

class OrderFragment: BaseFragment<FragmentOrderBinding>(R.layout.fragment_order) {

    override fun haveToolbar(): Boolean = true
    override fun resToolbarId(): Int = R.id.toolbar

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val homeViewModel: HomeViewModel by navGraphViewModels(R.id.cure_health_care_nav_host_xml) { viewModelFactory }
    private lateinit var orderAdapter : OrderAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel.resetData()
        lifecycleScope.launchWhenCreated {
            homeViewModel.orderRequest.postValue(
                Event("")
            )
        }
        lifecycleScope.launch {
            orderAdapter = OrderAdapter(requireContext())
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = homeViewModel

        mBinding.orderRecyclerView.adapter = orderAdapter
        mBinding.orderRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        mBinding.orderRecyclerView.itemAnimator = DefaultItemAnimator()


        homeViewModel.orderEvent.observe(viewLifecycleOwner,EventObserver{
            if(getInt(it) != 0 && homeViewModel.orderPageCount.value == 1){
                orderAdapter.updateList(homeViewModel.orderList)
            }else{
                orderAdapter.addList(homeViewModel.orderList)
            }
        })

        OrderAdapter.navigate = {
            homeViewModel.singleOrderRequest.postValue(Event(it))
        }

        homeViewModel.singleOrderEvent.observe(viewLifecycleOwner,EventObserver{
            if(it == Status.SUCCESS.name){
                navigate(OrderFragmentDirections.actionOrderToSingleOrderFragment())
            }
        })

        mBinding.orderRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1)) {
                    homeViewModel.orderRequest.postValue(
                        Event(
                            (homeViewModel.pageCount.value!!+1).toString()
                        )
                    )
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        val callBack : OnBackPressedCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                backToHome()
            }
        }
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,callBack)

        mBinding.toolbar.setNavigationOnClickListener {
            backToHome()
        }
    }
}