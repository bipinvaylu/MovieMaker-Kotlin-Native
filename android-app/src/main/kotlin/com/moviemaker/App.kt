package com.moviemaker

import android.app.Application
import com.moviemaker.settings.SettingsRepository
import com.moviemaker.utils.settingsRepository

class App : Application() {

    init {
        Companion.instance = App()
    }


    companion object {
        private lateinit var instance: App

        //TODO: Use dagger module
        val settingsRepo: SettingsRepository by lazy {
            settingsRepository(instance)
        }
    }
}