package com.devshawon.curehealthcare.ui.fragments.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.navGraphViewModels
import com.devshawon.curehealthcare.R
import com.devshawon.curehealthcare.base.ui.BaseFragment
import com.devshawon.curehealthcare.dagger.viewModel.AppViewModelFactory
import com.devshawon.curehealthcare.databinding.FragmentProfileBinding
import com.devshawon.curehealthcare.ui.CureHealthCareActivity
import com.devshawon.curehealthcare.ui.auth.AuthActivity
import com.devshawon.curehealthcare.ui.auth.AuthActivityScreen
import com.devshawon.curehealthcare.ui.fragments.HomeViewModel
import com.devshawon.curehealthcare.util.navigate
import javax.inject.Inject

class ProfileFragment : BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {
    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val homeViewModel: HomeViewModel by navGraphViewModels(R.id.cure_health_care_nav_host_xml) { viewModelFactory }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = homeViewModel

        mBinding.nameText.text = preferences.customerName
        mBinding.shopNameText.text = preferences.shopAddress
        mBinding.numberText.text = preferences.mobileNumber

        mBinding.profileLayout.setOnClickListener {
            navigate(ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment())
        }
        mBinding.changePasswordLayout.setOnClickListener {
            navigate(ProfileFragmentDirections.actionProfileFragmentToChagePassword())
        }
        mBinding.helpLineLayout.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:01860998499")
            startActivity(intent)
        }

        mBinding.logoutLayout.setOnClickListener {
            preferences.isLogin = false
            //val mScreen = AuthActivityScreen(true, R.id.login_fragment)
            //activityScreenSwitcher()?.open(mScreen)
            val intent = Intent(requireContext(),AuthActivity :: class.java)
            startActivity(intent)
            (activity as CureHealthCareActivity).finish()
        }

        val callBack : OnBackPressedCallback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                backToHome()
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,callBack)
    }

}