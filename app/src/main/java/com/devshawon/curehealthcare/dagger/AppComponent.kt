package com.devshawon.curehealthcare.dagger

import com.devshawon.curehealthcare.CureHealthCareApplication
import com.devshawon.curehealthcare.dagger.modules.ActivityBindingModule
import com.devshawon.curehealthcare.dagger.modules.apiModule.ApiModule
import com.devshawon.curehealthcare.dagger.modules.app.AppModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidSupportInjectionModule::class, AppModule::class, ActivityBindingModule::class,ApiModule::class]
)
interface AppComponent : AndroidInjector<CureHealthCareApplication> {
    @Component.Factory
    interface Factory : AndroidInjector.Factory<CureHealthCareApplication>
}