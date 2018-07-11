package com.moviemaker

import android.app.Application
import com.moviemaker.utils.Prefs

class App : Application() {

    init {
        Companion.instance = App()
    }


    companion object {
        private lateinit var instance: App

        //TODO: Use dagger module
        val prefs: Prefs by lazy {
            Prefs(instance)
        }
    }
}