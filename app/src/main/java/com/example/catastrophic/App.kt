package com.example.catastrophic

import android.app.Application
import com.example.catastrophic.di.AppComponent
import com.example.catastrophic.di.DaggerAppComponent


class App : Application() {

    var appComponent: AppComponent? = null
        private set

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
                .applicationContext(applicationContext)
                .build()

    }
}