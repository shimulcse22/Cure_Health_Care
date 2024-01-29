package com.devshawon.curehealthcare.ui.fragments.filter

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devshawon.curehealthcare.R
import com.devshawon.curehealthcare.base.ui.BaseFragment
import com.devshawon.curehealthcare.dagger.viewModel.AppViewModelFactory
import com.devshawon.curehealthcare.databinding.NotificationFragmentBinding
import com.devshawon.curehealthcare.models.ProductRequest
import com.devshawon.curehealthcare.network.Status
import com.devshawon.curehealthcare.ui.fragments.HomeViewModel
import com.devshawon.curehealthcare.useCase.result.Event
import com.devshawon.curehealthcare.useCase.result.EventObserver
import kotlinx.coroutines.launch
import javax.inject.Inject

class NotificationFragment : BaseFragment<NotificationFragmentBinding>(R.layout.notification_fragment) {
    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val viewModel: HomeViewModel by navGraphViewModels(R.id.cure_health_care_nav_host_xml) { viewModelFactory }
    private lateinit var notificationAdapter: NotificationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.resetData()
        notificationAdapter = NotificationAdapter(requireContext())
        lifecycleScope.launch {
            viewModel.notificationRequest.postValue(Event(viewModel.notificationPageCount.value?:0))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = viewModel

        mBinding.notificationRecyclerView.adapter = notificationAdapter
        mBinding.notificationRecyclerView.itemAnimator = DefaultItemAnimator()
        mBinding.notificationRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.productEvent.observe(viewLifecycleOwner,EventObserver{
            if(it == Status.SUCCESS.name && viewModel.notificationPageCount.value == 1){
                notificationAdapter.updateData(viewModel.notificationList)
            }else{
                notificationAdapter.updateMoreData(viewModel.notificationList)
            }
        })

        notificationAdapter.markCount.observe(viewLifecycleOwner,EventObserver{
            Log.d("notificationssss","$it")
            if(it == 1){
                mBinding.markAsRead.visibility = View.VISIBLE
            }
        })

        mBinding.markAsRead.setOnClickListener {
            var count = 0
            viewModel.notificationList.forEach {
                if(it.status == "Unread" ){
                    count++
                    it.status = ""
                }
            }
            if (count >0) mBinding.markAsRead.visibility = View.GONE
            notificationAdapter.notifyItemRangeChanged(0,count)
        }

        mBinding.notificationRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1)) {
                    viewModel.notificationRequest.postValue(
                        Event(
                            viewModel.notificationPageCount.value!!+1
                        )
                    )
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        mBinding.toolbar.setNavigationOnClickListener {
            backToHome()
        }
    }
}