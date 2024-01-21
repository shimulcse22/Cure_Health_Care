package com.devshawon.curehealthcare.ui.auth.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.navGraphViewModels
import com.devshawon.curehealthcare.R
import com.devshawon.curehealthcare.base.ui.BaseFragment
import com.devshawon.curehealthcare.dagger.viewModel.AppViewModelFactory
import com.devshawon.curehealthcare.databinding.RegistrationFragmentLayoutBinding
import com.devshawon.curehealthcare.models.RegistrationRequest
import com.devshawon.curehealthcare.ui.auth.AuthViewModel
import com.devshawon.curehealthcare.useCase.result.EventObserver
import com.devshawon.curehealthcare.util.navigateUp
import com.devshawon.curehealthcare.util.positiveButton
import com.devshawon.curehealthcare.util.showDialog
import javax.inject.Inject

class RegistrationFragment :
    BaseFragment<RegistrationFragmentLayoutBinding>(R.layout.registration_fragment_layout) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val viewModel: AuthViewModel by navGraphViewModels(R.id.auth_nav_graph) { viewModelFactory }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = viewModel

        mBinding.registerBtn.setOnClickListener {
            viewModel.regRequest.postValue(
                RegistrationRequest(
                    address = viewModel.shopAddress.value ?: "",
                    fname = viewModel.firstName.value ?: "",
                    lname = viewModel.lastName.value ?: "",
                    license = viewModel.license.value ?: "",
                    mobile = viewModel.mobile.value ?: "",
                    nid = viewModel.nid.value ?: "",
                    shop = viewModel.shopName.value ?: ""
                )
            )
        }
        mBinding.cancelBtn.setOnClickListener {

        }

        mBinding.alreadyRegistered.setOnClickListener {
            navigateUp()
        }

        viewModel.event.observe(viewLifecycleOwner,EventObserver{
            showDialog {
                setTitle(getString(R.string.error_title))
                setMessage(it)
                setIcon(R.drawable.ic_error)
                positiveButton(getString(R.string.ok))
            }
        })
    }
}