package com.devshawon.curehealthcare.ui.auth.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.navGraphViewModels
import com.devshawon.curehealthcare.R
import com.devshawon.curehealthcare.base.ui.BaseFragment
import com.devshawon.curehealthcare.dagger.viewModel.AppViewModelFactory
import com.devshawon.curehealthcare.databinding.SplashFragmentBinding
import com.devshawon.curehealthcare.models.LoginRequest
import com.devshawon.curehealthcare.network.Status
import com.devshawon.curehealthcare.ui.CureHealthCareActivityScreen
import com.devshawon.curehealthcare.ui.auth.AuthViewModel
import com.devshawon.curehealthcare.useCase.result.EventObserver
import com.devshawon.curehealthcare.util.navigate
import com.devshawon.curehealthcare.util.positiveButton
import com.devshawon.curehealthcare.util.showDialog
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashFragment : BaseFragment<SplashFragmentBinding>(R.layout.splash_fragment) {
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

        mBinding.registerBtn.setOnClickListener {
            navigate(SplashFragmentDirections.actionSplashFragmentToRegistrationFragment())
        }
        mBinding.loginBtn.setOnClickListener {
            navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
        }

        mBinding.callUs.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:01860998499")
            startActivity(intent)
        }
    }
}