package com.devshawon.curehealthcare

import com.devshawon.curehealthcare.base.BaseApplication
import com.devshawon.curehealthcare.dagger.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class CureHealthCareApplication  : BaseApplication(){
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> = DaggerAppComponent.factory().create(this)
}