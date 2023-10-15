package com.devshawon.curehealthcare.dagger.modules.app

import android.content.Context
import com.devshawon.curehealthcare.CureHealthCareApplication
import dagger.Module
import dagger.Provides

@Module
class AppModule {
    @Provides
    fun provideApplicationContext(mApplication: CureHealthCareApplication): Context =
        mApplication.applicationContext
}