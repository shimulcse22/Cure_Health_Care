package com.devshawon.curehealthcare.ui.auth

import android.content.Intent
import android.os.Bundle
import com.devshawon.curehealthcare.base.navigation.ActivityScreen
import com.devshawon.curehealthcare.base.ui.BaseActivity
import kotlin.reflect.KClass

class AuthActivityScreen constructor(
    private val needClearStack: Boolean,
    private val navigationId: Int, private val bundle: Bundle? = null
) : ActivityScreen() {

    companion object {
        private const val NEED_TO_CLEAR_STACK = "AuthenticationActivityScreen.ClearStack"
        const val NAVIGATION_ID = "AuthenticationActivityScreen.NAVIGATION_ID"
    }

    override fun activityClass(): KClass<out BaseActivity<*>> = AuthActivity::class

    override
    fun configureIntent(intent: Intent) {
        intent.putExtra(NAVIGATION_ID, navigationId)
        if (needClearStack) {
            intent.flags = (Intent.FLAG_ACTIVITY_NO_ANIMATION
                    or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    or Intent.FLAG_ACTIVITY_NEW_TASK
                    or Intent.FLAG_ACTIVITY_CLEAR_TASK)

            if (bundle != null) {
                intent.putExtras(bundle)
            }

            intent.putExtra(NEED_TO_CLEAR_STACK, needClearStack)
        }
    }
}