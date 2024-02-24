package com.devshawon.curehealthcare.ui.fragments.order

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devshawon.curehealthcare.R
import com.devshawon.curehealthcare.base.ui.BaseFragment
import com.devshawon.curehealthcare.dagger.viewModel.AppViewModelFactory
import com.devshawon.curehealthcare.databinding.FragmentOrderBinding
import com.devshawon.curehealthcare.network.Status
import com.devshawon.curehealthcare.ui.OrderAdapter
import com.devshawon.curehealthcare.ui.fragments.HomeViewModel
import com.devshawon.curehealthcare.useCase.result.Event
import com.devshawon.curehealthcare.useCase.result.EventObserver
import com.devshawon.curehealthcare.util.getInt
import com.devshawon.curehealthcare.util.navigate
import com.devshawon.curehealthcare.util.navigateUp
import com.devshawon.curehealthcare.util.positiveButton
import com.devshawon.curehealthcare.util.showDialog
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

        lifecycleScope.launch {
            orderAdapter = OrderAdapter(requireContext())
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = homeViewModel

        homeViewModel.orderList.clear()

        mBinding.orderRecyclerView.adapter = orderAdapter
        mBinding.orderRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        mBinding.orderRecyclerView.itemAnimator = DefaultItemAnimator()

        lifecycleScope.launchWhenCreated {
            homeViewModel.orderRequest.postValue(
                Event(homeViewModel.orderPageCount.value!!)
            )
        }

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
            }else{
                showDialog {
                    setTitle(getString(R.string.error_title))
                    setMessage(it)
                    setIcon(R.drawable.ic_error)
                    positiveButton(getString(R.string.ok)){
                        navigateUp()
                    }
                }
            }
        })

        mBinding.orderRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1)) {
                    homeViewModel.orderRequest.postValue(
                        Event(
                            (homeViewModel.orderPageCount.value!!+1)
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