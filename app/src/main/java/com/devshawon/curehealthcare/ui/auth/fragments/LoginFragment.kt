package com.devshawon.curehealthcare.ui.auth.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.navGraphViewModels
import com.devshawon.curehealthcare.R
import com.devshawon.curehealthcare.base.ui.BaseFragment
import com.devshawon.curehealthcare.dagger.viewModel.AppViewModelFactory
import com.devshawon.curehealthcare.databinding.LoginFragmentLayoutBinding
import com.devshawon.curehealthcare.models.LoginRequest
import com.devshawon.curehealthcare.ui.CureHealthCareActivityScreen
import com.devshawon.curehealthcare.ui.auth.AuthActivityScreen
import com.devshawon.curehealthcare.ui.auth.AuthViewModel
import com.devshawon.curehealthcare.useCase.result.EventObserver
import timber.log.Timber
import javax.inject.Inject
import javax.net.ssl.SSLEngineResult.Status

class LoginFragment  : BaseFragment<LoginFragmentLayoutBinding>(R.layout.login_fragment_layout) {
    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val viewModel: AuthViewModel by navGraphViewModels(R.id.auth_nav_graph) { viewModelFactory }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = viewModel

        mBinding.loginBtn.setOnClickListener {
        Log.d("THE DATA IS CLICKED","j")
            viewModel.loginRequest.postValue(LoginRequest(
                phone = "01677732635",
                password = "770175"
            ))
        }

        viewModel.event.observe(viewLifecycleOwner,EventObserver{
            if(it == bd.com.upay.customer.network.Status.SUCCESS.name){
                activityScreenSwitcher()?.open(
                    CureHealthCareActivityScreen(
                        true
                    )
                )
            }
        })
    }
}