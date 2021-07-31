package com.irzstudio.gajian

import android.app.Application
import com.irzstudio.gajian.di.DataModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppController: Application() {
    override fun onCreate() {
        super.onCreate()

//        startKoin {
//            androidContext(this@AppController)
//            modules(dataModule)
//            modules(viewModelModule)
//        }
    }
}