package com.nyayasetu

import android.app.Application
import com.nyayasetu.utils.AppConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NyayaSetuApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppConfig.initialize(isDebug = BuildConfig.DEBUG)
    }
}
