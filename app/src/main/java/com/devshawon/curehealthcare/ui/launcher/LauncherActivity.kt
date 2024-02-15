package com.devshawon.curehealthcare.ui.launcher

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import com.devshawon.curehealthcare.BuildConfig
import com.devshawon.curehealthcare.R
import com.devshawon.curehealthcare.base.ui.BaseActivity
import com.devshawon.curehealthcare.dagger.viewModel.AppViewModelFactory
import com.devshawon.curehealthcare.databinding.LauncherActivityBinding
import com.devshawon.curehealthcare.ui.CureHealthCareActivity
import com.devshawon.curehealthcare.ui.auth.AuthActivity
import com.devshawon.curehealthcare.ui.auth.AuthViewModel
import com.devshawon.curehealthcare.useCase.result.EventObserver
import com.devshawon.curehealthcare.util.SharedPreferenceStorage
import com.devshawon.curehealthcare.util.positiveButton
import com.devshawon.curehealthcare.util.showDialog
import javax.inject.Inject

class LauncherActivity : BaseActivity<LauncherActivityBinding>(R.layout.launcher_activity) {
    lateinit var preferences: SharedPreferenceStorage

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    private val viewModel by viewModels<AuthViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences = SharedPreferenceStorage(applicationContext)
        mBinding.viewModel = viewModel

        viewModel.versionReq.value = (Unit)

        viewModel.check.observe(this@LauncherActivity, EventObserver {
            if (it == BuildConfig.VERSION_NAME && viewModel.isTrue.value == true) {
                if (preferences.isLogin) {
                    val intent = Intent(this, CureHealthCareActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val intent = Intent(this, AuthActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } else if (it != BuildConfig.VERSION_NAME && viewModel.isTrue.value == true) {
                showDialog(cancelable = false, cancelableTouchOutside = false) {
                    setTitle(getString(R.string.update_available))
                    setMessage(getString(R.string.force_update_text))
                    setIcon(R.drawable.logo)
                    positiveButton(getString(R.string.ok)) {
                        goToPlayStore()
                    }
                    setCancelable(false)
                }
            }
        })
    }

    private fun goToPlayStore() {
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=${BuildConfig.APPLICATION_ID}")
                )
            )
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}")
                )
            )
        }
    }
}