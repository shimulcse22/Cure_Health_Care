package com.devshawon.curehealthcare.ui.auth.fragments

import android.os.Bundle
import android.view.View
import androidx.navigation.navGraphViewModels
import com.devshawon.curehealthcare.R
import com.devshawon.curehealthcare.base.ui.BaseFragment
import com.devshawon.curehealthcare.dagger.viewModel.AppViewModelFactory
import com.devshawon.curehealthcare.databinding.ForgotPasswordFragmentBinding
import com.devshawon.curehealthcare.models.ForgotPasswordRequest
import com.devshawon.curehealthcare.ui.auth.AuthViewModel
import com.devshawon.curehealthcare.useCase.result.EventObserver
import com.devshawon.curehealthcare.util.navigateUp
import com.devshawon.curehealthcare.util.positiveButton
import com.devshawon.curehealthcare.util.showDialog
import javax.inject.Inject

class ForgotPasswordFragment : BaseFragment<ForgotPasswordFragmentBinding>(R.layout.forgot_password_fragment){
    @Inject
    lateinit var viewModelFactory: AppViewModelFactory
    private val viewModel: AuthViewModel by navGraphViewModels(R.id.auth_nav_graph) { viewModelFactory }

    override fun haveToolbar(): Boolean {
        return false
    }

    override fun resToolbarId(): Int {
        return 0
    }

    val list = setOf(
        "012","013","014","015","016","017","018","019"
    )


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.viewModel = viewModel

        viewModel.phone.value = ""

        mBinding.backToBtn.setOnClickListener {
            navigateUp()
        }

        mBinding.loginBtn.setOnClickListener {
            if(viewModel.phone.value?.length !=11 && !list.contains(viewModel.phone.value?.take(3))){
                showDialog {
                    setTitle(getString(R.string.error_title))
                    setMessage("Please Provide correct mobile number")
                    setIcon(R.drawable.ic_error)
                    positiveButton(getString(R.string.ok))
                }

                return@setOnClickListener
            }
            viewModel.forgotRequest.postValue(ForgotPasswordRequest(
                phone = viewModel.phone.value
            ))
        }

        viewModel.event.observe(viewLifecycleOwner, EventObserver{
            showDialog {
                setTitle(getString(R.string.error_title))
                setMessage(it)
                setIcon(R.drawable.ic_error)
                positiveButton(getString(R.string.ok))
            }
        })
    }
}