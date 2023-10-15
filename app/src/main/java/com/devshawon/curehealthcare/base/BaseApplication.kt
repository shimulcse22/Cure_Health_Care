package com.devshawon.curehealthcare.base

import android.content.Context
import com.crashlytics.android.Crashlytics
import dagger.android.support.DaggerApplication
import io.fabric.sdk.android.Fabric
import timber.log.Timber

abstract class BaseApplication  : DaggerApplication(){
    lateinit var context : Context
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        initTimber()
        initCrashReport()
    }

    private fun initTimber() {
//        if (BuildConfig) {
//            val mdDebugTree = Timber.DebugTree()
//            Timber.plant(mdDebugTree)
//        } else {
            val mCrashReportingTree = CrashReportingTree()
            Timber.plant(mCrashReportingTree)
        //}
    }

    private fun initCrashReport() {
        Fabric.with(this, Crashlytics())
        Thread.setDefaultUncaughtExceptionHandler { _, ex -> Crashlytics.logException(ex) }
    }
}