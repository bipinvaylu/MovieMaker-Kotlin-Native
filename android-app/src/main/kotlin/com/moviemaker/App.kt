package com.moviemaker

import android.app.Application
import com.moviemaker.di.component.AppComponent
import com.moviemaker.di.component.DaggerAppComponent
import com.moviemaker.di.module.AppModule
import timber.log.Timber

class App : Application() {

    private val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }
    init {
        Companion.instance = this
        //TODO: Plant tree if debug build
        Timber.plant(Timber.DebugTree())
        Timber.d("Bipin - App init called")
    }


    companion object {
        // TODO: Fix lateinit issue
        private lateinit var instance: App

        val component: AppComponent
            get() = instance.appComponent

//        //TODO: Use dagger module
//        val prefsRepo: SettingsRepository by lazy {
//            Timber.d("Bipin - prefsRepo init called")
//            settingsRepository(instance)
//        }
//
//        //TODO: Use dagger module
//        val picasso: Picasso by lazy {
//            Picasso.Builder(instance).build()
//        }
    }
}