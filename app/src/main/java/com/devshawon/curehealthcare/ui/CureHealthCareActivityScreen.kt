package com.devshawon.curehealthcare.ui

import android.content.Intent
import com.devshawon.curehealthcare.base.navigation.ActivityScreen
import com.devshawon.curehealthcare.base.ui.BaseActivity
import kotlin.reflect.KClass

class CureHealthCareActivityScreen constructor(private val needClearStack: Boolean) : ActivityScreen() {

    companion object {
        private const val NEED_TO_CLEAR_STACK = "CustomerActivityScreen.ClearStack"
    }

    override fun activityClass(): KClass<out BaseActivity<*>> = CureHealthCareActivity::class

    override
    fun configureIntent(intent: Intent) {
        if (needClearStack) {
            intent.flags = (Intent.FLAG_ACTIVITY_NO_ANIMATION
                    or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    or Intent.FLAG_ACTIVITY_NEW_TASK
                    or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.putExtra(NEED_TO_CLEAR_STACK, needClearStack)
        }
    }
}
