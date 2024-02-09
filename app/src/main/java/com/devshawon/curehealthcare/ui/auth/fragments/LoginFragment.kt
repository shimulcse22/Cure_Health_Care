package com.devshawon.curehealthcare.ui.auth.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import com.devshawon.curehealthcare.R
import com.devshawon.curehealthcare.api.Message
import com.devshawon.curehealthcare.base.ui.BaseFragment
import com.devshawon.curehealthcare.dagger.viewModel.AppViewModelFactory
import com.devshawon.curehealthcare.databinding.LoginFragmentLayoutBinding
import com.devshawon.curehealthcare.models.LoginRequest
import com.devshawon.curehealthcare.ui.CureHealthCareActivityScreen
import com.devshawon.curehealthcare.ui.auth.AuthViewModel
import com.devshawon.curehealthcare.useCase.result.EventObserver
import com.devshawon.curehealthcare.util.navigate
import com.devshawon.curehealthcare.util.positiveButton
import com.devshawon.curehealthcare.util.showDialog
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginFragment : BaseFragment<LoginFragmentLayoutBinding>(R.layout.login_fragment_layout) {
    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val viewModel: AuthViewModel by navGraphViewModels(R.id.auth_nav_graph) { viewModelFactory }
    override fun haveToolbar(): Boolean {
        return false
    }

    override fun resToolbarId(): Int {
        return 0
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = viewModel
        viewModel.loader.value = false
        viewModel.phone.value = ""

        mBinding.loginBtn.setOnClickListener {
            viewModel.loader.value = true
            viewModel.loginRequest.postValue(
                LoginRequest(
                    phone = "01677732635", password = "995633"
                )
            )
        }

        viewModel.event.observe(viewLifecycleOwner, EventObserver {
            //mBinding.loadingStateProgressCircular.visibility = View.GONE
            if (it == com.devshawon.curehealthcare.network.Status.SUCCESS.name) {
                activityScreenSwitcher()?.open(
                    CureHealthCareActivityScreen(
                        false
                    )
                )
                preferences.isLogin = true
            } else {
                showDialog {
                    setTitle(getString(R.string.error_title))
                    setMessage(it)
                    setIcon(R.drawable.ic_error)
                    positiveButton(getString(R.string.ok))
                }
            }
        })

        mBinding.registerBtn.setOnClickListener {
            navigate(LoginFragmentDirections.actionLoginFragmentToRegistrationFragment())
        }

        mBinding.forgotPasswordBtn.setOnClickListener {
            navigate(LoginFragmentDirections.actionLoginFragmentToForgotFragment())
        }
    }
}