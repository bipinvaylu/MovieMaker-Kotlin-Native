package com.moviemaker

import android.app.Application
import com.moviemaker.domain.Media
import com.moviemaker.settings.SettingsRepository
import com.moviemaker.utils.settingsRepository
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import timber.log.Timber

class App : Application() {

    init {
//        Companion.instance = App()
        //TODO: Plant tree if debug build
        Timber.plant(Timber.DebugTree())
    }


    companion object {
        // TODO: Fix lateinit issue
        private var instance: App = App()

        //TODO: Use dagger module
        val settingsRepo: SettingsRepository by lazy {
            settingsRepository(instance)
        }

        val moshi: Moshi by lazy {
            Moshi.Builder().build()
        }

        val mediaAdapter: JsonAdapter<List<Media>> by lazy {
            val type = Types.newParameterizedType(Media::class.java)
            moshi.adapter<List<Media>>(type)
        }

    }
}