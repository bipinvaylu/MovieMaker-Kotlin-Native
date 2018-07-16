package com.moviemaker

import android.app.Application
import com.moviemaker.settings.SettingsRepository
import com.moviemaker.utils.settingsRepository
import com.squareup.picasso.Picasso
import timber.log.Timber

class App : Application() {

    init {
        Companion.instance = this
        //TODO: Plant tree if debug build
        Timber.plant(Timber.DebugTree())
        Timber.d("Bipin - App init called")
    }


    companion object {
        // TODO: Fix lateinit issue
        private lateinit var instance: App

        //TODO: Use dagger module
        val settingsRepo: SettingsRepository by lazy {
            Timber.d("Bipin - settingsRepo init called")
            settingsRepository(instance)
        }

        val picasso: Picasso by lazy {
            Picasso.Builder(instance).build()
        }
    }
}