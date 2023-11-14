package com.painandpanic.blossombuddy

import android.app.Application
import com.painandpanic.blossombuddy.di.roomModule
import com.painandpanic.blossombuddy.di.useCaseModule
import com.painandpanic.blossombuddy.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BlossomBuddyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BlossomBuddyApp)
            modules(listOf(useCaseModule, viewModelModule, roomModule))
        }
    }
}