package com.devshawon.curehealthcare.ui.fragments.profile

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.navGraphViewModels
import com.devshawon.curehealthcare.R
import com.devshawon.curehealthcare.base.ui.BaseFragment
import com.devshawon.curehealthcare.dagger.viewModel.AppViewModelFactory
import com.devshawon.curehealthcare.databinding.ChnagePasswordFragmentBinding
import com.devshawon.curehealthcare.models.UpdatePassword
import com.devshawon.curehealthcare.ui.fragments.HomeViewModel
import com.devshawon.curehealthcare.useCase.result.EventObserver
import javax.inject.Inject

class ChangePassword : BaseFragment<ChnagePasswordFragmentBinding>(R.layout.chnage_password_fragment) {
    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val homeViewModel: HomeViewModel by navGraphViewModels(R.id.cure_health_care_nav_host_xml) { viewModelFactory }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = homeViewModel

        mBinding.updatePassword.setOnClickListener{
            Log.d("THE UPDATE IS ","${homeViewModel.newPassword.value}")
            homeViewModel.updatePasswordRequest.postValue(
                UpdatePassword(
                    oldPassword = homeViewModel.oldPassword.value!!,
                    newPassword = homeViewModel.newPassword.value!!,
                    newPasswordConfirmation = homeViewModel.confirmNewPassword.value!!
                )
            )
        }

        homeViewModel.updatePasswordEvent.observe(viewLifecycleOwner,EventObserver{
            Toast.makeText(requireContext(),it,Toast.LENGTH_LONG).show()
        })
    }
}