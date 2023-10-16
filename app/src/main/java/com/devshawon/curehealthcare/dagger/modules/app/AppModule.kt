package com.devshawon.curehealthcare.dagger.modules.app

import android.content.Context
import com.devshawon.curehealthcare.CureHealthCareApplication
import com.devshawon.curehealthcare.base.navigation.ActivityScreenSwitcher
import com.devshawon.curehealthcare.util.PreferenceStorage
import com.devshawon.curehealthcare.util.SharedPreferenceStorage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Provides
    fun provideApplicationContext(mApplication: CureHealthCareApplication): Context =
        mApplication.applicationContext

    @Singleton
    @Provides
    fun provideActivityScreenSwitcher() = ActivityScreenSwitcher()
    @Singleton
    @Provides
    fun providesPreferenceStorage(context: Context): PreferenceStorage =
        SharedPreferenceStorage(context)
}