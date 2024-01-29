package com.devshawon.curehealthcare.ui.fragments.profile

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.navGraphViewModels
import com.devshawon.curehealthcare.R
import com.devshawon.curehealthcare.base.ui.BaseFragment
import com.devshawon.curehealthcare.dagger.viewModel.AppViewModelFactory
import com.devshawon.curehealthcare.databinding.EditProfileFragmentBinding
import com.devshawon.curehealthcare.databinding.FragmentProfileBinding
import com.devshawon.curehealthcare.models.UpdateProfileCustomer
import com.devshawon.curehealthcare.models.UpdateProfileRequest
import com.devshawon.curehealthcare.ui.fragments.HomeViewModel
import com.devshawon.curehealthcare.useCase.result.Event
import com.devshawon.curehealthcare.useCase.result.EventObserver
import com.devshawon.curehealthcare.util.navigateUp
import javax.inject.Inject

class EditProfileFragment : BaseFragment<EditProfileFragmentBinding>(R.layout.edit_profile_fragment) {
    override fun haveToolbar(): Boolean  = true
    override fun resToolbarId(): Int  = R.id.toolbar
    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val homeViewModel: HomeViewModel by navGraphViewModels(R.id.cure_health_care_nav_host_xml) { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel.editProfileEvent.postValue(Event(Unit))
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = homeViewModel

        mBinding.updateProfileBtn.setOnClickListener{
            val data = UpdateProfileCustomer()
            data.apply {
                firstName = homeViewModel.firstName.value
                lastName = homeViewModel.lastName.value
                license = homeViewModel.license.value
                nid = homeViewModel.license.value
                shopAddress = homeViewModel.shopAddress.value
                shopName = homeViewModel.shopName.value
            }
            homeViewModel.updateProfileRequest.postValue(
                UpdateProfileRequest(
                    method = "PUT",
                    customer = data,
                    phone = homeViewModel.mobile.value!!
                )
            )
        }

        homeViewModel.updateProfileEvent.observe(viewLifecycleOwner,EventObserver{
            Toast.makeText(requireContext(),it,Toast.LENGTH_LONG).show()
        })

        mBinding.toolbar.setNavigationOnClickListener {
            navigateUp()
        }
    }
}