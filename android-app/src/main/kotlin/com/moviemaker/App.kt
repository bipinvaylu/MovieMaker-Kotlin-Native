package com.moviemaker

import android.app.Application
import com.moviemaker.utils.Prefs
import com.squareup.moshi.Moshi

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

//        //TODO: Use dagger module
//        val moshi: Moshi by lazy {
//
//        }
    }
}