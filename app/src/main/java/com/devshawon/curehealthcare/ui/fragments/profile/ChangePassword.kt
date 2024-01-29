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
import com.devshawon.curehealthcare.util.navigate
import com.devshawon.curehealthcare.util.navigateUp
import javax.inject.Inject

class ChangePassword : BaseFragment<ChnagePasswordFragmentBinding>(R.layout.chnage_password_fragment) {
    override fun haveToolbar(): Boolean  = true
    override fun resToolbarId(): Int  = R.id.toolbar
    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val homeViewModel: HomeViewModel by navGraphViewModels(R.id.cure_health_care_nav_host_xml) { viewModelFactory }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = homeViewModel

        mBinding.updatePassword.setOnClickListener{
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

        mBinding.toolbar.setNavigationOnClickListener {
            navigateUp()
        }
    }
}