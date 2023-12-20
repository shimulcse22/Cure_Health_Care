package com.devshawon.curehealthcare.ui.auth.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.navGraphViewModels
import com.devshawon.curehealthcare.R
import com.devshawon.curehealthcare.base.ui.BaseFragment
import com.devshawon.curehealthcare.dagger.viewModel.AppViewModelFactory
import com.devshawon.curehealthcare.databinding.RegistrationFragmentLayoutBinding
import com.devshawon.curehealthcare.ui.auth.AuthViewModel
import com.devshawon.curehealthcare.util.navigate
import com.devshawon.curehealthcare.util.navigateUp
import javax.inject.Inject

class RegistrationFragment : BaseFragment<RegistrationFragmentLayoutBinding>(R.layout.registration_fragment_layout) {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val viewModel: AuthViewModel by navGraphViewModels(R.id.auth_nav_graph) { viewModelFactory }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = viewModel

        mBinding.registerBtn.setOnClickListener {

        }
        mBinding.cancelBtn.setOnClickListener {

        }

        mBinding.alreadyRegistered.setOnClickListener{
            navigateUp()
        }
    }
}