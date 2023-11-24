package com.suvanl.fixmylinks

import android.app.Application
import com.suvanl.fixmylinks.di.AppContainer
import com.suvanl.fixmylinks.di.AppDataContainer
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FmlApplication : Application() {

    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}